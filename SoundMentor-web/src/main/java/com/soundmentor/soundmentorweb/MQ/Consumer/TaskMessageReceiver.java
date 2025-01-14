package com.soundmentor.soundmentorweb.MQ.Consumer;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.config.MqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.factory.TaskHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听所有任务结果
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TaskMessageReceiver {
    private final TaskHandlerFactory taskHandlerFactory;
    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME_TASK_BACK)//监听的队列名称 TestDirectQueue
    public void process(String taskResult) {
        log.info("收到任务结果：{}",taskResult);
        TaskMessageDTO taskMessageDTO = JSON.parseObject(taskResult, TaskMessageDTO.class);
        taskHandlerFactory.getTaskHandler(taskMessageDTO.getType()).taskDone(taskMessageDTO);
    }
}