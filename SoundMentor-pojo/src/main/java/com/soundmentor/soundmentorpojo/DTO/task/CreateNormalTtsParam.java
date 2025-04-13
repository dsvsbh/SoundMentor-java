package com.soundmentor.soundmentorpojo.DTO.task;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

@Data
public class CreateNormalTtsParam implements CreateTaskParam{
    private TaskTypeEnum type;
    private TaskTypeEnum taskType;
    private String content;
    private Integer userSoundId;
    private Integer rate;
}
