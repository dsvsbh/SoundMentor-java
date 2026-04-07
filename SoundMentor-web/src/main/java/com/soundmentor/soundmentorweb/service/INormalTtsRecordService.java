package com.soundmentor.soundmentorweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;

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
}