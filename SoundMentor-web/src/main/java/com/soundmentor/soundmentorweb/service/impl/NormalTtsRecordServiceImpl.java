package com.soundmentor.soundmentorweb.service.impl;



import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DO.PresetSoundDO;
import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsGenerateReqDTO;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsRecordResDTO;
import com.soundmentor.soundmentorweb.mapper.NormalTtsRecordMapper;
import com.soundmentor.soundmentorweb.mapper.PresetSoundMapper;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorweb.service.TTSService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
@Slf4j
@Service
public class NormalTtsRecordServiceImpl extends ServiceImpl<NormalTtsRecordMapper, NormalTtsRecord> implements INormalTtsRecordService {
    @Resource
    private PresetSoundMapper presetSoundMapper;
    
    @Resource
    private TTSService ttsService;
    
    @Resource
    private UserInfoApi userInfoApi;

    @Override
    public List<PresetSoundDTO> listVoices() {
        // 查询所有预设声音
        List<PresetSoundDO> presetSoundDOList = presetSoundMapper.selectList(null);
        
        // DO转DTO
        List<PresetSoundDTO> presetSoundDTOList = new ArrayList<>();
        for (PresetSoundDO presetSoundDO : presetSoundDOList) {
            PresetSoundDTO dto = new PresetSoundDTO();
            dto.setId(presetSoundDO.getId());
            dto.setSoundUrl(presetSoundDO.getSoundUrl());
            dto.setSoundName(presetSoundDO.getSoundName());
            dto.setApiParam(presetSoundDO.getApiParam());
            dto.setDescription(presetSoundDO.getDescription());
            presetSoundDTOList.add(dto);
        }
        
        return presetSoundDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NormalTtsRecordResDTO generateAudio(NormalTtsGenerateReqDTO reqDTO) {
        // 验证文本长度
        if (StringUtils.isBlank(reqDTO.getContent())) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "文本内容不能为空");
        }
        if (reqDTO.getContent().length() > 1000) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "文本内容不能超过1000字");
        }

        // 验证语速范围
        if (reqDTO.getSpeed() == null || reqDTO.getSpeed() < 0.5f || reqDTO.getSpeed() > 2.0f) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "语速必须在0.5-2.0之间");
        }

        // 将字符串转换为枚举值
        AudioParameters.Voice voice;
        try {
            voice = AudioParameters.Voice.valueOf(reqDTO.getVoiceName());
        } catch (IllegalArgumentException e) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "无效的声音类型: " + reqDTO.getVoiceName());
        }

        // 生成任务名称（文本前6个字+……）
        String taskName;
        if (reqDTO.getContent().length() > 6) {
            taskName = reqDTO.getContent().substring(0, 6) + "……";
        } else {
            taskName = reqDTO.getContent();
        }

        // 调用TTS服务生成音频
        String audioUrl = ttsService.textToSpeech(voice, reqDTO.getContent(), reqDTO.getSpeed());

        // 获取当前用户ID
        Integer userId = userInfoApi.getUser().getId();

        // 保存记录
        NormalTtsRecord record = new NormalTtsRecord();
        record.setUserId(userId);
        record.setFileName(taskName + ".wav");
        record.setFileUrl(audioUrl);
        record.setSpeed(reqDTO.getSpeed());
        record.setVoiceName(reqDTO.getVoiceName());
        record.setContent(reqDTO.getContent());
        record.setCreateTime(LocalDateTime.now());
        
        this.save(record);

        // 转换为DTO返回
        return convertToDTO(record, taskName);
    }

    @Override
    public PageResult<NormalTtsRecordResDTO> getRecordsByPage(Integer pageNum, Integer pageSize) {
        // 获取当前用户ID
        Integer userId = userInfoApi.getUser().getId();

        // 构建查询条件
        LambdaQueryWrapper<NormalTtsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NormalTtsRecord::getUserId, userId)
                .orderByDesc(NormalTtsRecord::getCreateTime);

        // 分页查询
        Page<NormalTtsRecord> page = new Page<>(pageNum, pageSize);
        Page<NormalTtsRecord> resultPage = this.page(page, queryWrapper);

        // 转换为DTO
        List<NormalTtsRecordResDTO> records = resultPage.getRecords().stream()
                .map(record -> {
                    String taskName;
                    if (record.getContent() != null && record.getContent().length() > 6) {
                        taskName = record.getContent().substring(0, 6) + "……";
                    } else {
                        taskName = record.getContent();
                    }
                    return convertToDTO(record, taskName);
                })
                .collect(Collectors.toList());

        // 构建分页结果
        return PageResult.<NormalTtsRecordResDTO>builder()
                .pageNum(resultPage.getCurrent())
                .pageSize(resultPage.getSize())
                .total(resultPage.getTotal())
                .pages(resultPage.getPages())
                .records(records)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "删除ID列表不能为空");
        }

        // 获取当前用户ID
        Integer userId = userInfoApi.getUser().getId();

        // 验证这些记录是否属于当前用户
        LambdaQueryWrapper<NormalTtsRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(NormalTtsRecord::getId, ids)
                .eq(NormalTtsRecord::getUserId, userId);
        
        long count = this.count(queryWrapper);
        if (count != ids.size()) {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "部分记录不存在或无权限删除");
        }

        // 批量删除
        this.removeByIds(ids);
    }

    /**
     * 将DO转换为DTO
     */
    private NormalTtsRecordResDTO convertToDTO(NormalTtsRecord record, String taskName) {
        return NormalTtsRecordResDTO.builder()
                .id(record.getId())
                .taskName(taskName)
                .fileName(record.getFileName())
                .fileUrl(record.getFileUrl())
                .speed(record.getSpeed())
                .voiceName(record.getVoiceName())
                .content(record.getContent())
                .createTime(record.getCreateTime())
                .build();
    }
}