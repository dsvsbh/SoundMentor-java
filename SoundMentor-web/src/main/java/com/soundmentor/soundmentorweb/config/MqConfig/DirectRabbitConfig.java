package com.soundmentor.soundmentorweb.config.MqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连交换机，一对一
 * 创建交换机、队列、绑定关系
 * @Author: Make
 * @DATE: 2025/01/10
 **/
@Configuration
public class DirectRabbitConfig {
    public static final String QUEUE_NAME_SOUND_TRAIN = "SoundTrainDirectQueue";
    public static final String EXCHANGE_NAME_SOUND_TRAIN = "SoundTrainDirectExchange";
    public static final String ROUTING_KEY_SOUND_TRAIN = "SoundTrainDirectRouting";
    /**
     * 创建队列
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    public Queue TestDirectQueue() {
        /*
         * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
         * exclusive:是否独占,默认是false,只能被当前创建的连接使用，而且当连接关闭后队列即被删除。
         * autoDelete:是否自动删除,默认是false,当没有生产者或者消费者使用此队列，该队列会自动删除。
         */
        return new Queue(QUEUE_NAME_SOUND_TRAIN,true);
    }

    /**
     * 创建Direct交换机
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME_SOUND_TRAIN,true,false);
    }

    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：SoundTrainDirectRouting
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with(ROUTING_KEY_SOUND_TRAIN);
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }
}