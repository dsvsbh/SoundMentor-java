package com.soundmentor.soundmentorweb.common.handler;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateVoiceTrainParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.biz.TaskBiz;
import com.soundmentor.soundmentorweb.biz.UserSoundBiz;
import com.soundmentor.soundmentorweb.service.ITaskService;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
@Slf4j
public class VoiceTrainHandler implements TaskHandler{
    @Resource
    private UserSoundBiz userSoundBiz;
    @Resource
    private TaskBiz taskBiz;
    @Resource
    private ITaskService taskService;
    @Resource
    private IUserSoundRelService userSoundRelService;
    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.VOICE_TRAIN.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTimeoutTask(TaskDO task) {
        if(task.getStatus().equals(TaskStatusEnum.CREATED.getCode())||task.getStatus().equals(TaskStatusEnum.RUNNING.getCode()))
        {
            log.info("声音库训练任务超时{}", task.getId());
            task.setStatus(TaskStatusEnum.FAIL.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("failReason", "声音库训练任务超时");
            task.setResult(JSON.toJSONString(result));
            taskService.updateById(task);
            String taskDetail = task.getTaskDetail();
            UserSoundRelDO userSoundRelDO = JSON.parseObject(taskDetail, UserSoundRelDO.class);
            userSoundRelDO.setStatus(TaskStatusEnum.FAIL.getCode());
            userSoundRelService.updateById(userSoundRelDO);
        }
    }
    @Override
    public void taskDone(TaskMessageDTO taskMessage) {
        String taskDetail = taskBiz.getTaskDetail(taskMessage.getId());
        // 更新声音表
        UserSoundRelDO userSoundRelDO = JSON.parseObject(taskDetail, UserSoundRelDO.class);
        userSoundRelDO.setStatus(taskMessage.getStatus());
        userSoundBiz.updateSound(userSoundRelDO);
        // 更新任务记录
        taskBiz.updateTask(taskMessage);
        log.info("声音训练任务执行结束：{}", userSoundRelDO.toString());
    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreateVoiceTrainParam param = (CreateVoiceTrainParam) createTaskParam;
        return userSoundBiz.addSound(param);
    }

    @Override
    public Integer timeLimit() {
        return 2*60*60*1000;
    }
}
