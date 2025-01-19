package com.soundmentor.soundmentorweb.common.handler;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.utils.PPTXUtil;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTPageSummaryTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptDetailMapper;
import com.soundmentor.soundmentorweb.service.PPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
@Slf4j
public class PPTSummaryHandler implements TaskHandler{
    @Resource
    private PPTService pptService;
    @Resource
    private UserPptDetailMapper userPptDetailMapper;
    @Resource
    private TaskMapper taskMapper;
    @Override
    public Integer getTaskType() {
        return TaskTypeEnum.PPT_SUMMARY.getCode();
    }

    @Override
    public void handleTimeoutTask(TaskDO task) {
        if(task.getStatus().equals(TaskStatusEnum.CREATED.getCode())||task.getStatus().equals(TaskStatusEnum.RUNNING.getCode()))
        {
            log.info("PPT页生成讲解任务超时{}", task.getId());
            task.setStatus(TaskStatusEnum.FAIL.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("failReason", "PPT页生成讲解任务超时");
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
            PPTPageSummaryTaskDTO pptPageSummaryTaskDTO = JSON.parseObject(taskMessage.getMessageBody().toString(), PPTPageSummaryTaskDTO.class);
            userPptDetailMapper.updateSummary(pptPageSummaryTaskDTO);
        }
    }

    @Override
    public Integer createTask(CreateTaskParam createTaskParam) {
        CreatePPTSummaryTaskParam param = (CreatePPTSummaryTaskParam) createTaskParam;
        return pptService.createPPTSummary(param);
    }

    @Override
    public Integer timeLimit() {
        return 5*60*1000;
    }


}
