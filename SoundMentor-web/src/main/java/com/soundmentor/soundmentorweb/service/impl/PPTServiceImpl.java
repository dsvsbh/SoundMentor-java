package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.PptTaskStatusEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.utils.DashScopeUtil;
import com.soundmentor.soundmentorbase.utils.PPTUtil;
import com.soundmentor.soundmentorpojo.DO.PptTaskDetailDO;
import com.soundmentor.soundmentorpojo.DO.PptTaskDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.BatchEditPPTVoiceExplanationDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PptTaskQueryResultDTO;
import com.soundmentor.soundmentorweb.config.properties.DashScopeProperties;
import com.soundmentor.soundmentorweb.mapper.PptTaskDetailMapper;
import com.soundmentor.soundmentorweb.mapper.PptTaskMapper;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.PPTService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PPTServiceImpl 有声ppt服务实现类 todo 待实现方法
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPPTTask(String url) {
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

        // 检查任务状态
        if (!PptTaskStatusEnum.CREATED.getCode().equals(task.getTaskStatus())) {
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
            StringBuilder failReason = new StringBuilder();

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
                        failReason.append("第").append(pageIndex + 1).append("页: ").append(e.getMessage()).append("; ");
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
                    failReason.append("部分任务超时未完成;");
                }

                // 6. 更新最终任务状态
                if (failureCount.get() > 0 || !completed) {
                    task.setTaskStatus(PptTaskStatusEnum.EXPLANATION_GENERATION_FAILED.getCode());
                    task.setFailReason(failReason.toString());
                    log.error("PPT讲解生成失败, taskId={}, 成功: {}, 失败: {}, 失败原因: {}",
                            taskId, successCount.get(), failureCount.get(), failReason.toString());
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
    public void batchEditExplanation(BatchEditPPTExplanationDTO batchEditPPTExplanationDTO) {

    }

    @Override
    public void generateExplanationVoice(Long taskId) {

    }

    @Override
    public void batchEditExplanationVoice(BatchEditPPTVoiceExplanationDTO batchEditPPTVoiceExplanationDTO) {

    }

    @Override
    public void generateSoundPPT(Long taskId) {

    }

    @Override
    public PptTaskQueryResultDTO queryTask(Long taskId) {
        return null;
    }

    @Override
    public List<PptTaskDTO> listTasks() {
        return Collections.emptyList();
    }
}
