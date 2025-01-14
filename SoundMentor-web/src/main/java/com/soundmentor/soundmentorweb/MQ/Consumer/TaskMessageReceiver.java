package com.soundmentor.soundmentorweb.MQ.Consumer;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.factory.TaskHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听所有任务结果
 */
@Component
@RequiredArgsConstructor
public class TaskMessageReceiver {
    private final TaskHandlerFactory taskHandlerFactory;
    @RabbitListener(queues = "TaskBack")//监听的队列名称 TestDirectQueue
    public void process(String taskResult) {
        TaskMessageDTO taskMessageDTO = JSON.parseObject(taskResult, TaskMessageDTO.class);
        taskHandlerFactory.getTaskHandler(taskMessageDTO.getType()).taskDone(taskMessageDTO);
    }
}