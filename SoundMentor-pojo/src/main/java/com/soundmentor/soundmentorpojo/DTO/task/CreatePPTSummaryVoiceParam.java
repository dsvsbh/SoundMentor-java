package com.soundmentor.soundmentorpojo.DTO.task;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

@Data
public class CreatePPTSummaryVoiceParam implements CreateTaskParam{
    private TaskTypeEnum type;
    private TaskTypeEnum taskType;
}
