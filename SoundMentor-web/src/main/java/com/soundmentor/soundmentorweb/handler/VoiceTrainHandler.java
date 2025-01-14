package com.soundmentor.soundmentorweb.handler;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateVoiceTrainParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.biz.UserSoundBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
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
        UserSoundRelDO userSoundRelDO = JSON.parseObject(taskMessage.getMessageBody().toString(), UserSoundRelDO.class);
        userSoundRelDO.setStatus(taskMessage.getStatus());
        userSoundBiz.updateSound(userSoundRelDO);
        log.info("声音训练任务执行成功：{}", userSoundRelDO.toString());
    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreateVoiceTrainParam param = (CreateVoiceTrainParam) createTaskParam;
        return userSoundBiz.addSound(param.getSoundPath());
    }
}
