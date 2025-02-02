package com.soundmentor.soundmentorweb.common.handler;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTSummaryVoiceMsgDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryVoiceParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptDetailMapper;
import com.soundmentor.soundmentorweb.service.PPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
@Slf4j
public class PPTSummaryVoiceHandler implements TaskHandler{
    @Resource
    private PPTService pptService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserPptDetailMapper userPptDetailMapper;

    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.PPT_SUMMARY_VOICE.getCode();
    }

    @Override
    public void handleTimeoutTask(TaskDO task) {
        if(task.getStatus().equals(TaskStatusEnum.CREATED.getCode())||task.getStatus().equals(TaskStatusEnum.RUNNING.getCode()))
        {
            log.info("PPT页生成讲解音频任务超时{}", task.getId());
            task.setStatus(TaskStatusEnum.FAIL.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("failReason", "PPT页生成讲解音频任务超时");
            task.setResult(JSON.toJSONString(result));
            taskMapper.updateById(task);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskDone(TaskMessageDTO taskMessage) {
        Integer status = taskMessage.getStatus();
        TaskDO taskDO = taskMapper.selectById(taskMessage.getId());
        taskDO.setStatus(status);
        taskDO.setResult(JSON.toJSONString(taskMessage.getMessageBody()));
        taskMapper.updateById(taskDO);
        if(TaskStatusEnum.SUCCESS.getCode().equals(status))
        {
            PPTSummaryVoiceMsgDTO dto = JSON.parseObject(taskMessage.getMessageBody().toString(), PPTSummaryVoiceMsgDTO.class);
            userPptDetailMapper.updateSoundUrl(dto);
        }
    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreatePPTSummaryVoiceParam param = (CreatePPTSummaryVoiceParam) createTaskParam;
        return pptService.createPPTSummaryVoice(param);
    }

    @Override
    public Integer timeLimit() {
        return 10*60*1000;
    }


}
