package com.soundmentor.soundmentorweb.common.mq.consumer;

import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorweb.common.factory.TaskHandlerFactory;
import com.soundmentor.soundmentorweb.common.handler.TaskHandler;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DelayMessageListener {
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskHandlerFactory taskHandlerFactory;

    /**
     * 延时消息监听，处理到期任务
     * @param message
     */
    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME_DELAY)
    public void handle(String message) {
        log.info("接收到延时消息：message ->{}", message);
        Integer taskId = Integer.valueOf(message);
        TaskDO taskDO = taskMapper.selectById(taskId);
        taskHandlerFactory.getTaskHandler(taskDO.getType()).handleTimeoutTask(taskDO);
    }
}
