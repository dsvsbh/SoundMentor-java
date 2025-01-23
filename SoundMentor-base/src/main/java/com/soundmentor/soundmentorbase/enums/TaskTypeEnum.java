package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeEnum {
    VOICE_TRAIN(1, "语音训练"),
    PPT_SUMMARY(2, "ppt总结生成"),
    PPT_SUMMARY_VOICE(3, "PPT讲解生成语音"),
    ;
    private final Integer code;
    private final String desc;
}
