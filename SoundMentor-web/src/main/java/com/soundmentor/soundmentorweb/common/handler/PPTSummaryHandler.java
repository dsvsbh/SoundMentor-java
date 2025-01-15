package com.soundmentor.soundmentorweb.common.handler;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.utils.PPTXUtil;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.service.PPTService;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class PPTSummaryHandler implements TaskHandler{
    @Resource
    private PPTService pptService;
    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.PPT_SUMMARY.getCode();
    }

    @Override
    public void handleTimeoutTask(Integer taskId) {

    }

    @Override
    public void taskDone(TaskMessageDTO taskMessage) {

    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreatePPTSummaryTaskParam param = (CreatePPTSummaryTaskParam) createTaskParam;
        return pptService.createPPTSummary(param);
    }


}
