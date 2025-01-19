package com.soundmentor.soundmentorweb.common.handler;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class PPTSummaryVoiceHandler implements TaskHandler{
    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.PPT_SUMMARY_VOICE.getCode();
    }

    @Override
    public void handleTimeoutTask(TaskDO task) {

    }

    @Override
    public void taskDone(TaskMessageDTO taskMessage) {

    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        return 0;
    }

    @Override
    public Integer timeLimit() {
        return 0;
    }


}
