package com.soundmentor.soundmentorweb.common.handler;

import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.factory.TaskHandlerFactory;

import javax.annotation.PostConstruct;

/**
 * 任务处理器接口，定义处理任务的方法
 */
public interface TaskHandler {
    @PostConstruct
    default void init() {
        // bean 初始化后，注册到任务处理器工厂中
        TaskHandlerFactory.register(this);
    }

    /**
     * 获取任务类型
     * @return
     */
    Integer getTaskType();

    /**
     * 处理超时任务
     * @param task
     */
    void handleTimeoutTask(TaskDO task);

    /**
     * 任务完成
     * @param taskMessage
     */
    void taskDone(TaskMessageDTO taskMessage);
    /**
     * 创建任务
     * @param param
     * @return
     */
    Integer createTask(CreateTaskParam param);

    /**
     * 任务执行的时间限制(ms)
     * @return
     */
    Integer timeLimit();
}
