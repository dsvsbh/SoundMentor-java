package com.soundmentor.soundmentorweb.handler;

import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateVoiceTrainParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import com.soundmentor.soundmentorweb.biz.UserSoundBiz;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VoiceTrainHandler implements TaskHandler{
    @Resource
    private UserSoundBiz userSoundBiz;
    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.VOICE_TRAIN.getCode();
    }

    @Override
    public void handleTimeoutTask(Integer taskId) {

    }

    @Override
    public void taskDone(TaskMessageDTO taskMessage) {

    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreateVoiceTrainParam param = (CreateVoiceTrainParam) createTaskParam;
        return userSoundBiz.addSound(param.getSoundPath());
    }
}
