package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeEnum {
    VOICE_TRAIN(1, "语音训练"),
    PPT_EXPLAIN(2, "PPT讲解"),
    PPT_EXPLAIN_VOICE(3, "PPT讲解生成有声ppt"),
    ;
    private final Integer code;
    private final String desc;
}
