package com.soundmentor.soundmentorweb.common.handler;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryVoiceParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.service.PPTService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PPTSummaryVoiceHandler implements TaskHandler{
    @Resource
    private PPTService pptService;
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
        CreatePPTSummaryVoiceParam param = (CreatePPTSummaryVoiceParam) createTaskParam;
        return pptService.createPPTSummaryVoice(param);
    }

    @Override
    public Integer timeLimit() {
        return 0;
    }


}
