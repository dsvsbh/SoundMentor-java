package com.soundmentor.soundmentorpojo.DTO.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateVoiceTrainParam implements CreateTaskParam{
    /**
     * 类型
     **/
    @NotNull(message = "type不能为空")
    private TaskTypeEnum type;
    /**
     * 任务类型
     **/
    @NotNull(message = "taskType不能为空")
    private TaskTypeEnum taskType;
    /**
     * 声音地址
     **/
    @NotNull(message = "soundPath不能为空")
    private String soundPath;
    /**
     * 声音名称
     **/
    @NotNull(message = "soundName不能为空")
    private String soundName;
}
