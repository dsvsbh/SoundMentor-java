package com.soundmentor.soundmentorpojo.DTO.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

/**
 * 创建任务参数，传实现类对象即可，根据type字段识别
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateVoiceTrainParam.class, name = "VOICE_TRAIN"),
        @JsonSubTypes.Type(value = CreatePPTSummaryTaskParam.class, name = "PPT_SUMMARY"),
        @JsonSubTypes.Type(value = CreatePPTSummaryVoiceParam.class, name = "PPT_SUMMARY_VOICE"),
        @JsonSubTypes.Type(value = CreateNormalTtsParam.class, name = "NORMAL_TTS")
})
public interface CreateTaskParam {
   TaskTypeEnum getTaskType();
}
