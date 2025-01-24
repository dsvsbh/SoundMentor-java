package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.Data;

@Data
public class PPTSummaryVoiceMsgDTO {
    private Integer userPptId;
    private Integer page;
    private String text;
    private String voiceName;
    private Integer rate;
}
