package com.soundmentor.soundmentorweb.biz;

import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.factory.TaskHandlerFactory;
import com.soundmentor.soundmentorweb.service.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 任务相关业务逻辑
 * @Author: Make
 * @DATE: 2025/01/14
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskBiz {
    @Resource
    private ITaskService taskService;
    private final TaskHandlerFactory taskHandlerFactory;

    /**
     * 根据id获取任务信息
     * @PARAM: @param id
     * @RETURN: @return 返回结果统一封装
     **/
    public TaskMessageDTO<String> getTaskById(Integer id) {
        TaskDO taskDO = taskService.getById(id);
        TaskMessageDTO<String> res = new TaskMessageDTO();
        res.setId(taskDO.getId());
        res.setCreateTime(taskDO.getCreateTime());
        res.setStatus(taskDO.getStatus());
        res.setType(taskDO.getType());
        res.setMessageBody(taskDO.getResult());
        return res;
    }

    /**
     * 获取任务详情
     * @PARAM: @param id
     * @RETURN: @return
     **/
    public String getTaskDetail(Integer id) {
        TaskDO taskDO = taskService.getById(id);
        return taskDO.getTaskDetail();
    }

    /**
     * 创建任务
     * @PARAM: @param param
     * @RETURN: @return
     **/
    public Integer createTask(CreateTaskParam param) {
        return taskHandlerFactory.getTaskHandler(param.getTaskType().getCode()).createTask(param);
    }

    /**
     * 更新任务记录
     * @PARAM: @param taskMessage
     * @RETURN: @return
     **/
    public Boolean updateTask(TaskMessageDTO taskMessage) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(taskMessage.getId());
        taskDO.setStatus(taskMessage.getStatus());
        taskDO.setResult(taskMessage.getMessageBody().toString());
        taskDO.setUpdateTime(LocalDateTime.now());
        return taskService.updateById(taskDO);
    }
}
