package com.soundmentor.soundmentorweb.MQ.Producer;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * MQ发送消息,泛型表示消息体类型
 * @Author: Make
 * @DATE: 2025/01/10
 **/
@Component
@Slf4j
public class MqProducer<T> {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param routingKey
     * @param message
     * @PARAM: @param exchangeName
     * @RETURN:
     **/
    public void send(String exchangeName, String routingKey, TaskMessageDTO<T> message){
        String messageString = JSON.toJSONString(message);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageString);
    }
}
