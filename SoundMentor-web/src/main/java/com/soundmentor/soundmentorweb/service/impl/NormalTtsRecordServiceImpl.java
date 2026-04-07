package com.soundmentor.soundmentorweb.service.impl;



import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DO.PresetSoundDO;
import com.soundmentor.soundmentorpojo.DTO.PresetSoundDTO;
import com.soundmentor.soundmentorweb.mapper.NormalTtsRecordMapper;
import com.soundmentor.soundmentorweb.mapper.PresetSoundMapper;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
@Service
public class NormalTtsRecordServiceImpl extends ServiceImpl<NormalTtsRecordMapper, NormalTtsRecord> implements INormalTtsRecordService {
    @Resource
    private PresetSoundMapper presetSoundMapper;

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
}