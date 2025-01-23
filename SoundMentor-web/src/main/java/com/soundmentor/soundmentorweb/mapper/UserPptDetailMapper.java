package com.soundmentor.soundmentorweb.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DO.UserPptDetailDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTPageSummaryTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTSummaryVoiceMsgDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTTaskResultDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-15
 */
public interface UserPptDetailMapper extends BaseMapper<UserPptDetailDO> {

    void updateSummary(PPTPageSummaryTaskDTO pptPageSummaryTaskDTO);

    void updateResult(PPTTaskResultDTO pptTaskResultDTO);

    void updateSoundUrl(PPTSummaryVoiceMsgDTO dto);
}
