package com.soundmentor.soundmentorweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsGenerateReqDTO;
import com.soundmentor.soundmentorpojo.DTO.tts.NormalTtsRecordResDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
public interface INormalTtsRecordService extends IService<NormalTtsRecord> {

    /**
     * 查询声音库
     * @return 预设声音列表
     */
    List<PresetSoundDTO> listVoices();

    /**
     * 生成预设文本朗读音频
     * @param reqDTO 生成请求DTO
     * @return 生成的记录
     */
    NormalTtsRecordResDTO generateAudio(NormalTtsGenerateReqDTO reqDTO);

    /**
     * 分页查询当前用户的预设文本朗读历史记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<NormalTtsRecordResDTO> getRecordsByPage(Integer pageNum, Integer pageSize);

    /**
     * 批量删除预设文本朗读记录
     * @param ids 记录ID列表
     */
    void batchDelete(List<Integer> ids);
}