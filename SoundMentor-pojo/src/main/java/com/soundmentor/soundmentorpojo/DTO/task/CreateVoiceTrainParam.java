package com.soundmentor.soundmentorpojo.DTO.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

@Data
public class CreateVoiceTrainParam implements CreateTaskParam{
    private TaskTypeEnum type;
    private TaskTypeEnum taskType;//传两个相同的type，一个用于反序列化，一个获取类型
    private String soundPath;
}
