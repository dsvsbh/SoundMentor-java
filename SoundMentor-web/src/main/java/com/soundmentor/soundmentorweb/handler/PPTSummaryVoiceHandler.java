package com.soundmentor.soundmentorweb.handler;

import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class PPTSummaryVoiceHandler implements TaskHandler{
    @Override
    public Integer getTaskType() {
        return 0;
    }

    @Override
    public void handleTimeoutTask(Integer taskId) {

    }

    @Override
    public void taskDone(TaskMessageDTO taskMessage) {

    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        return 0;
    }


}
