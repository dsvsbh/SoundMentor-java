package com.soundmentor.soundmentorweb.common.mq.producer;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : coderzyj
 * @version : 1.0
 * @description : 延时任务消息发送
 */
@Component
@Slf4j
public class DelayTaskMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message 消息体
     * @param delay   延迟时间
     */
    public void sendDelayMessage(String message, Integer delay) {
        log.info("发送延时任务消息：{},延迟时间：{}", JSON.toJSONString(message), delay);
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME_DELAY,DirectRabbitConfig.ROUTING_KEY_DELAY,
                message, message1 -> {
                    message1.getMessageProperties().setDelay(delay);
                    return message1;
                });
    }
}
