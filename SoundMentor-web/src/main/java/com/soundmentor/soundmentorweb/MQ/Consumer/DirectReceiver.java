package com.soundmentor.soundmentorweb.MQ.Consumer;

import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DirectReceiver {
    @RabbitListener(queues = "SoundTrainDirectQueue")//监听的队列名称 TestDirectQueue
    public void process(String testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage);
    }
}