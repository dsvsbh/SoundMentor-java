package com.soundmentor.soundmentorweb.MQ.Producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * MQ发送消息
 * @Author: Make
 * @DATE: 2025/01/10
 **/
@Component
public class MqProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param routingKey
     * @param map
     * @PARAM: @param exchangeName
     * @RETURN:
     **/
    public void send(String exchangeName,String routingKey,Map<String,Object> map){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, map);
    }
}
