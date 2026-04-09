package com.soundmentor.soundmentorweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.PptTaskStatusEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.utils.DashScopeUtil;
import com.soundmentor.soundmentorbase.utils.PPTUtil;
import com.soundmentor.soundmentorpojo.DO.PptTaskDetailDO;
import com.soundmentor.soundmentorpojo.DO.PptTaskDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.EditPPTVoiceExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.EditPPTExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.GenerateVoiceDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskQueryResultDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDetailDTO;
import com.soundmentor.soundmentorweb.config.properties.DashScopeProperties;
import com.soundmentor.soundmentorweb.mapper.PptTaskDetailMapper;
import com.soundmentor.soundmentorweb.mapper.PptTaskMapper;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.PPTService;
import com.soundmentor.soundmentorweb.service.TTSService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * PPTServiceImpl 有声ppt服务实现类
 */
@Service
@Slf4j
public class PPTServiceImpl implements PPTService {

    @Resource(name = "task-thread-pool-executor")
    private  ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private  UserInfoApi userInfoApi;
    @Resource
    private PptTaskMapper pptTaskMapper;
    @Resource
    private PptTaskDetailMapper pptTaskDetailMapper;
    @Resource
    private FileService fileService;
    @Resource
    private DashScopeProperties dashScopeProperties;
    @Resource
    private TTSService ttsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPPTTask(String url, String taskName) {
        if (StringUtils.isBlank(url)) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "PPT文件URL不能为空");
        }

        Long userId = Long.valueOf(userInfoApi.getUser().getId());

        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(url);
        List<? extends Slide<?, ?>> slides = PPTUtil.getSlides(slideShow);
        if (slides == null || slides.isEmpty()) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "PPT文件为空，没有幻灯片页");
        }

        PptTaskDO task = new PptTaskDO();
        task.setUserId(userId);
        task.setOriginalPptFileUrl(url);
        // 如果没有提供任务名称，设置为默认值
        task.setTaskName(StringUtils.isNotBlank(taskName) ? taskName : "未命名任务");
        task.setTaskStatus(PptTaskStatusEnum.CREATED.getCode());
        task.setTotalPages(slides.size());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        pptTaskMapper.insert(task);

        Long taskId = task.getId();
        List<PptTaskDetailDO> detailList = new ArrayList<>();

        for (int i = 0; i < slides.size(); i++) {
            Slide<?, ?> slide = slides.get(i);
            try {
                InputStream imageStream = PPTUtil.convertSlideToImage(slide);
                String md5 = UUID.randomUUID().toString().replace("-", "");
                String imgUrl = fileService.uploadFileToMinio(imageStream, FileTypeEnum.PNG, md5);
                imageStream.close();

                PptTaskDetailDO detail = new PptTaskDetailDO();
                detail.setTaskId(taskId);
                detail.setPageNumber(i + 1);
                detail.setImgUrl(imgUrl);
                detailList.add(detail);
            } catch (Exception e) {
                log.error("生成第{}页预览图失败", i + 1, e);
                throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "生成第" + (i + 1) + "页预览图失败: " + e.getMessage());
            }
        }

        for (PptTaskDetailDO detail : detailList) {
            pptTaskDetailMapper.insert(detail);
        }

        log.info("创建PPT任务成功, taskId={}, 总页数={}", taskId, slides.size());
        return taskId;
    }

    @Override
    public void generateExplanation(Long taskId) {
        // 1. 查询任务
        PptTaskDO task = pptTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BizException(ResultCodeEnum.DATA_NOT_FUND.getCode(), "任务不存在");
        }

        // 检查任务状态 是否为创建或讲解生成失败
        if (!(PptTaskStatusEnum.CREATED.getCode().equals(task.getTaskStatus())||PptTaskStatusEnum.EXPLANATION_GENERATION_FAILED.getCode().equals(task.getTaskStatus()))) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务状态不正确，当前状态: " + PptTaskStatusEnum.getByCode(task.getTaskStatus()).getDescription());
        }

        // 2. 查询每页的任务详情
        List<PptTaskDetailDO> detailList = pptTaskDetailMapper.selectList(
            new LambdaQueryWrapper<PptTaskDetailDO>()
                .eq(PptTaskDetailDO::getTaskId, taskId)
                .orderByAsc(PptTaskDetailDO::getPageNumber)
        );

        if (detailList == null || detailList.isEmpty()) {
            throw new BizException(ResultCodeEnum.DATA_NOT_FUND.getCode(), "任务详情不存在");
        }

        // 3. 加载 PPT
        String pptUrl = task.getOriginalPptFileUrl();
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptUrl);
        List<? extends Slide<?, ?>> slides = PPTUtil.getSlides(slideShow);

        // 4. 更新任务状态为"讲解生成中"
        task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATING.getCode());
        task.setUpdatedAt(LocalDateTime.now());
        pptTaskMapper.updateById(task);

        // 5. 异步并发处理
        threadPoolExecutor.execute(() -> {
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(slides.size());
            AtomicReference<String> failReason = new AtomicReference<>(null);

            for (int i = 0; i < slides.size(); i++) {
                final int pageIndex = i;
                final Slide<?, ?> slide = slides.get(i);
                final PptTaskDetailDO detail = detailList.get(i);

                threadPoolExecutor.execute(() -> {
                    try {
                        // 解析 PPT 页面内容
                        String slideContent = PPTUtil.getSlideInfo(slide);

                        // 请求大模型生成讲解
                        String explanation = DashScopeUtil.generatePPTExplanation(dashScopeProperties.getApiKey(), slideContent);

                        // 更新任务详情
                        detail.setExplanationText(explanation);
                        pptTaskDetailMapper.updateById(detail);

                        successCount.incrementAndGet();
                        log.info("第{}页讲解生成成功", pageIndex + 1);
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        log.error("第{}页讲解生成失败", pageIndex + 1, e);
                        failReason.set("部分页面讲解生成失败");
                    } finally {
                        latch.countDown();
                    }
                });
            }

            try {
                // 等待所有任务完成（设置超时时间为每页30秒，总共最多5分钟）
                boolean completed = latch.await(Math.max(60, slides.size() * 30), TimeUnit.SECONDS);

                if (!completed) {
                    log.warn("PPT讲解生成超时，已完成: {}/{}", slides.size() - latch.getCount(), slides.size());
                    failReason.set("部分页面讲解生成超时");
                }

                // 6. 更新最终任务状态
                if (failureCount.get() > 0 || !completed) {
                    task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATION_FAILED.getCode());
                    task.setFailReason(failReason.get() != null ? failReason.get() : "讲解生成失败");
                    log.error("PPT讲解生成失败, taskId={}, 成功: {}, 失败: {}, 失败原因: {}",
                            taskId, successCount.get(), failureCount.get(), failReason.get() != null ? failReason.get() : "讲解生成失败");
                } else {
                    task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATED.getCode());
                    task.setFailReason(null);
                    log.info("PPT讲解生成成功, taskId={}, 总页数: {}", taskId, slides.size());
                }

                task.setUpdatedAt(LocalDateTime.now());
                pptTaskMapper.updateById(task);

            } catch (InterruptedException e) {
                log.error("等待讲解生成任务时被中断", e);
                Thread.currentThread().interrupt();
                task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATION_FAILED.getCode());
                task.setFailReason("任务被中断: " + e.getMessage());
                task.setUpdatedAt(LocalDateTime.now());
                pptTaskMapper.updateById(task);
            } catch (Exception e) {
                log.error("讲解生成过程中发生异常", e);
                task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATION_FAILED.getCode());
                task.setFailReason("系统异常: " + e.getMessage());
                task.setUpdatedAt(LocalDateTime.now());
                pptTaskMapper.updateById(task);
            }
        });

        log.info("PPT讲解生成任务已提交异步处理, taskId={}", taskId);
    }

    @Override
    public void editExplanation(EditPPTExplanationDTO editPPTExplanationDTO) {
        Long pptTaskDetailId = editPPTExplanationDTO.getPptTaskDetailId();
        String newExplanation = editPPTExplanationDTO.getNewExplanation();
        
        // 检查任务详情是否存在
        PptTaskDetailDO detail = pptTaskDetailMapper.selectById(pptTaskDetailId);
        if (detail == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务详情不存在");
        }
        
        // 检查任务是否属于当前用户
        PptTaskDO task = pptTaskMapper.selectById(detail.getTaskId());
        if (task == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务不存在");
        }
        
        Long userId = Long.valueOf(userInfoApi.getUser().getId());
        if (!userId.equals(task.getUserId())) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "没有权限编辑该任务");
        }
        
        // 更新讲解内容
        detail.setExplanationText(newExplanation);
        pptTaskDetailMapper.updateById(detail);
    }

    @Override
    public void generateExplanationVoice(GenerateVoiceDTO generateVoiceDTO) {
        Long taskId = generateVoiceDTO.getTaskId();
        String voice = generateVoiceDTO.getVoice();
        Float speed = generateVoiceDTO.getSpeed();
        
        // 校验速度范围
        if (speed == null || speed < 0.5f || speed > 2.0f) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "语速必须在0.5到2.0之间");
        }
        
        // 校验voice
        if (voice == null || voice.isEmpty()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "声音类型不能为空");
        }
        
        // 1. 查询任务
        PptTaskDO task = pptTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务不存在");
        }
        
        // 检查任务状态（讲解已生成或讲解语音生成失败后才能生成语音）
        if (!(PptTaskStatusEnum.EXPLANATION_GENERATED.getCode().equals(task.getTaskStatus())||PptTaskStatusEnum.EXPLANATION_VOICE_GENERATION_FAILED.getCode().equals(task.getTaskStatus()))) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务状态不正确，当前状态: " + PptTaskStatusEnum.getByCode(task.getTaskStatus()).getDescription());
        }
        
        // 2. 查询每页的任务详情（需要有讲解内容的）
        List<PptTaskDetailDO> detailList = pptTaskDetailMapper.selectList(
            new LambdaQueryWrapper<PptTaskDetailDO>()
                .eq(PptTaskDetailDO::getTaskId, taskId)
                .isNotNull(PptTaskDetailDO::getExplanationText)
                .orderByAsc(PptTaskDetailDO::getPageNumber)
        );
        
        if (detailList == null || detailList.isEmpty()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "没有可生成语音的讲解内容");
        }
        
        // 3. 更新任务状态为"讲解语音生成中"
        task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_VOICE_GENERATING.getCode());
        task.setUpdatedAt(LocalDateTime.now());
        pptTaskMapper.updateById(task);
        
        // 4. 异步串行处理
        threadPoolExecutor.execute(() -> {
            int successCount = 0;
            int failureCount = 0;
            String failReason = null;
            
            // 串行执行每页的语音生成
            for (PptTaskDetailDO detail : detailList) {
                try {
                    // 请求TTS服务生成语音
                    String audioUrl = ttsService.textToSpeech(
                        com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters.Voice.valueOf(voice),
                        detail.getExplanationText(),
                        speed
                    );
                    
                    // 更新任务详情
                    detail.setExplanationAudioUrl(audioUrl);
                    pptTaskDetailMapper.updateById(detail);
                    
                    successCount++;
                    log.info("任务{}第{}页语音生成成功", taskId, detail.getPageNumber());
                } catch (Exception e) {
                    failureCount++;
                    log.error("任务{}第{}页语音生成失败: {}", taskId, detail.getPageNumber(), e.getMessage(), e);
                    failReason = "部分页面语音生成失败";
                }
            }
            
            // 5. 更新最终任务状态
                if (failureCount > 0) {
                    task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_VOICE_GENERATION_FAILED.getCode());
                    task.setFailReason(failReason != null ? failReason : "语音生成失败");
                    log.error("PPT语音生成失败, taskId={}, 成功: {}, 失败: {}, 失败原因: {}",
                            taskId, successCount, failureCount, failReason != null ? failReason : "语音生成失败");
                } else {
                    task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_VOICE_GENERATED.getCode());
                    task.setFailReason(null);
                    log.info("PPT语音生成成功, taskId={}, 总页数: {}", taskId, detailList.size());
                }
            
            task.setUpdatedAt(LocalDateTime.now());
            pptTaskMapper.updateById(task);
        });
        
        log.info("PPT语音生成任务已提交异步处理, taskId={}", taskId);
    }

    @Override
    public void editExplanationVoice(EditPPTVoiceExplanationDTO editPPTVoiceExplanationDTO) {
        Long pptTaskDetailId = editPPTVoiceExplanationDTO.getPptTaskDetailId();
        String newVoicePath = editPPTVoiceExplanationDTO.getNewVoicePath();
        
        // 检查任务详情是否存在
        PptTaskDetailDO detail = pptTaskDetailMapper.selectById(pptTaskDetailId);
        if (detail == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务详情不存在");
        }
        
        // 检查任务是否属于当前用户
        PptTaskDO task = pptTaskMapper.selectById(detail.getTaskId());
        if (task == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务不存在");
        }
        
        Long userId = Long.valueOf(userInfoApi.getUser().getId());
        if (!userId.equals(task.getUserId())) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "没有权限编辑该任务");
        }
        
        // 更新讲解语音
        detail.setExplanationAudioUrl(newVoicePath);
        pptTaskDetailMapper.updateById(detail);
    }

    @Override
    public void generateSoundPPT(Long taskId) {
        // 1. 查询任务
        PptTaskDO task = pptTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务不存在");
        }

        // 检查任务状态（语音已生成或有声ppt生成失败后才能生成有声PPT）
        if (!(PptTaskStatusEnum.EXPLANATION_VOICE_GENERATED.getCode().equals(task.getTaskStatus())||PptTaskStatusEnum.AUDIO_PPT_GENERATION_FAILED.getCode().equals(task.getTaskStatus()))) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "任务状态不正确，当前状态: " + PptTaskStatusEnum.getByCode(task.getTaskStatus()).getDescription());
        }

        // 2. 查询每页的任务详情（需要有音频的）
        List<PptTaskDetailDO> detailList = pptTaskDetailMapper.selectList(
            new LambdaQueryWrapper<PptTaskDetailDO>()
                .eq(PptTaskDetailDO::getTaskId, taskId)
                .isNotNull(PptTaskDetailDO::getExplanationAudioUrl)
                .orderByAsc(PptTaskDetailDO::getPageNumber)
        );

        if (detailList == null || detailList.isEmpty()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "没有可嵌入的音频文件");
        }

        // 3. 更新任务状态为"有声PPT生成中"
        task.setTaskStatus(PptTaskStatusEnum.AUDIO_PPT_GENERATING.getCode());
        task.setUpdatedAt(LocalDateTime.now());
        pptTaskMapper.updateById(task);

        // 4. 异步处理生成有声PPT
        threadPoolExecutor.execute(() -> {
            java.io.File tempDir = null;
            try {
                // 使用PPTUtil直接加载原始PPT文件
                SlideShow<?, ?> slideShow = PPTUtil.loadPPT(task.getOriginalPptFileUrl());
                
                // 下载所有音频文件并保存到临时文件夹
                tempDir = new java.io.File(System.getProperty("java.io.tmpdir"), 
                    "ppt_audio_" + taskId + "_" + System.currentTimeMillis());
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }
                
                // 为每个有音频的幻灯片添加音频
                for (PptTaskDetailDO detail : detailList) {
                    if (detail.getExplanationAudioUrl() != null) {
                        // 下载音频文件到临时文件
                        byte[] audioBytes = downloadFileFromUrl(detail.getExplanationAudioUrl());
                        String audioFileName = "page_" + detail.getPageNumber() + ".wav";
                        java.io.File audioFile = new java.io.File(tempDir, audioFileName);
                        
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(audioFile)) {
                            fos.write(audioBytes);
                        }
                        
                        // 使用相对路径添加音频到幻灯片
                        String relativeAudioPath = audioFileName; // 相对路径
                        int slideIndex = detail.getPageNumber() - 1;
                        PPTUtil.addAudioToSlide(slideShow, slideIndex, relativeAudioPath);
                        
                        log.info("音频已添加到幻灯片 {}: {}", detail.getPageNumber(), relativeAudioPath);
                    }
                }
                
                // 确定输出文件名
                String originalUrl = task.getOriginalPptFileUrl().toLowerCase();
                String pptFileName = originalUrl.endsWith(".pptx") ? 
                    "audio_ppt_" + taskId + ".pptx" : 
                    "audio_ppt_" + taskId + ".ppt";
                
                // 将修改后的PPT写入临时文件夹
                java.io.File pptFile = new java.io.File(tempDir, pptFileName);
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(pptFile)) {
                    slideShow.write(fos);
                }
                
                // 压缩整个临时目录为zip文件
                java.io.File zipFile = new java.io.File(System.getProperty("java.io.tmpdir"), 
                    "audio_ppt_" + taskId + ".zip");
                createZipFile(tempDir, zipFile);
                
                // 读取zip文件到字节数组
                byte[] zipBytes = java.nio.file.Files.readAllBytes(zipFile.toPath());
                
                // 上传生成的zip文件
                String audioPptUrl = fileService.uploadFileToMinio(
                    new ByteArrayInputStream(zipBytes),
                    FileTypeEnum.ZIP,
                    "audio_ppt_" + taskId + ".zip"
                );
                
                // 删除临时zip文件
                zipFile.delete();
                
                // 更新任务状态为成功
                task.setTaskStatus(PptTaskStatusEnum.AUDIO_PPT_GENERATED.getCode());
                task.setAudioPptFileUrl(audioPptUrl);
                task.setUpdatedAt(LocalDateTime.now());
                pptTaskMapper.updateById(task);
                
                log.info("有声PPT生成成功, taskId={}, 生成文件URL={}", taskId, audioPptUrl);
                
                // 清理临时文件
                if (tempDir != null && tempDir.exists()) {
                    deleteDirectory(tempDir);
                }
                
            } catch (Exception e) {
                log.error("生成有声PPT失败, taskId={}", taskId, e);
                
                // 更新任务状态为失败
                task.setTaskStatus(PptTaskStatusEnum.AUDIO_PPT_GENERATION_FAILED.getCode());
                task.setFailReason("生成有声PPT失败: " + e.getMessage());
                task.setUpdatedAt(LocalDateTime.now());
                pptTaskMapper.updateById(task);
                
                // 清理临时文件
                try {
                    if (tempDir != null && tempDir.exists()) {
                        deleteDirectory(tempDir);
                    }
                } catch (Exception ex) {
                    log.warn("清理临时文件失败: {}", ex.getMessage());
                }
            }
        });
        
        log.info("有声PPT生成任务已提交异步处理, taskId={}", taskId);
    }
    
    // 辅助方法：通过网络URL下载文件
    
    // 辅助方法：删除目录
    private void deleteDirectory(java.io.File directory) {
        if (directory.exists()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    
    // 辅助方法：创建zip文件
    private void createZipFile(java.io.File sourceDir, java.io.File zipFile) throws Exception {
        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFile))) {
            addFilesToZip(sourceDir, sourceDir, zos);
        }
    }
    
    // 辅助方法：添加文件到zip
    private void addFilesToZip(java.io.File rootDir, java.io.File sourceDir, java.util.zip.ZipOutputStream zos) throws Exception {
        java.io.File[] files = sourceDir.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    addFilesToZip(rootDir, file, zos);
                } else {
                    String relativePath = rootDir.toURI().relativize(file.toURI()).getPath();
                    java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(relativePath);
                    zos.putNextEntry(entry);
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                    zos.closeEntry();
                }
            }
        }
    }
    
    // 辅助方法：通过网络URL下载文件
    private byte[] downloadFileFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("下载失败，响应码: " + responseCode);
            }
            
            // 使用Java 8兼容的方式读取输入流
            try (InputStream in = conn.getInputStream()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                return out.toByteArray();
            }
            
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileUrl, e);
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "下载文件失败: " + e.getMessage());
        }
    }

    @Override
    public PptTaskQueryResultDTO queryTask(Long taskId) {
        // 查询任务信息
        PptTaskDO task = pptTaskMapper.selectById(taskId);
        if (task == null) {
            return null;
        }
        
        // 查询任务详情
        List<PptTaskDetailDO> detailList = pptTaskDetailMapper.selectList(
            new LambdaQueryWrapper<PptTaskDetailDO>()
                .eq(PptTaskDetailDO::getTaskId, taskId)
                .orderByAsc(PptTaskDetailDO::getPageNumber)
        );
        
        // 转换为DTO
        PptTaskQueryResultDTO resultDTO = new PptTaskQueryResultDTO();
        resultDTO.setId(task.getId())
                .setUserId(task.getUserId())
                .setOriginalPptFileUrl(task.getOriginalPptFileUrl())
                .setTaskName(task.getTaskName())
                .setTaskStatus(task.getTaskStatus())
                .setAudioPptFileUrl(task.getAudioPptFileUrl())
                .setTotalPages(task.getTotalPages())
                .setCreatedAt(task.getCreatedAt())
                .setUpdatedAt(task.getUpdatedAt());
        
        // 转换详情列表
        if (detailList != null && !detailList.isEmpty()) {
            List<PptTaskDetailDTO> detailDTOList = new ArrayList<>();
            for (PptTaskDetailDO detail : detailList) {
                PptTaskDetailDTO detailDTO = new PptTaskDetailDTO();
                detailDTO.setId(detail.getId())
                        .setTaskId(detail.getTaskId())
                        .setPageNumber(detail.getPageNumber())
                        .setImgUrl(detail.getImgUrl())
                        .setExplanationText(detail.getExplanationText())
                        .setExplanationAudioUrl(detail.getExplanationAudioUrl());
                detailDTOList.add(detailDTO);
            }
            resultDTO.setDetailList(detailDTOList);
        }
        
        return resultDTO;
    }

    @Override
    public List<PptTaskDTO> listTasks() {
        Long userId = Long.valueOf(userInfoApi.getUser().getId());
        
        List<PptTaskDO> tasks = pptTaskMapper.selectList(
            new LambdaQueryWrapper<PptTaskDO>()
                .eq(PptTaskDO::getUserId, userId)
                .orderByDesc(PptTaskDO::getCreatedAt)
        );
        
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<PptTaskDTO> taskDTOList = new ArrayList<>();
        for (PptTaskDO task : tasks) {
            PptTaskDTO taskDTO = new PptTaskDTO();
            taskDTO.setId(task.getId())
                    .setTaskName(task.getTaskName())
                    .setTaskStatus(task.getTaskStatus())
                    .setTotalPages(task.getTotalPages())
                    .setOriginalPptFileUrl(task.getOriginalPptFileUrl())
                    .setAudioPptFileUrl(task.getAudioPptFileUrl())
                    .setCreatedAt(task.getCreatedAt())
                    .setUpdatedAt(task.getUpdatedAt());
            taskDTOList.add(taskDTO);
        }
        
        return taskDTOList;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteTasks(List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return;
        }
        
        Long userId = Long.valueOf(userInfoApi.getUser().getId());
        
        // 检查任务是否属于当前用户
        List<PptTaskDO> tasks = pptTaskMapper.selectList(
            new LambdaQueryWrapper<PptTaskDO>()
                .in(PptTaskDO::getId, taskIds)
                .eq(PptTaskDO::getUserId, userId)
        );
        
        if (tasks == null || tasks.isEmpty()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "没有权限删除指定任务");
        }
        
        // 批量删除任务详情
        for (Long taskId : taskIds) {
            pptTaskDetailMapper.delete(
                new LambdaQueryWrapper<PptTaskDetailDO>()
                    .eq(PptTaskDetailDO::getTaskId, taskId)
            );
        }
        
        // 批量删除任务
        pptTaskMapper.deleteBatchIds(taskIds);
    }
}