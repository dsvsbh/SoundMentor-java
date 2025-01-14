package com.soundmentor.soundmentorpojo.DTO.task;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import lombok.Data;

@Data
public class CreatePPTSummaryTaskParam implements CreateTaskParam{
    private TaskTypeEnum type;
    private TaskTypeEnum taskType;
    private String pptUrl;

}
