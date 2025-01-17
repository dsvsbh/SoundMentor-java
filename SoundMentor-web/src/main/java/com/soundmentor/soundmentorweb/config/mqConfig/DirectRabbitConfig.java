package com.soundmentor.soundmentorweb.config.mqConfig;

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
    // 声音训练队列
    public static final String QUEUE_NAME_SOUND_TRAIN = "SoundTrainDirectQueue";
    public static final String EXCHANGE_NAME_SOUND_TRAIN = "SoundTrainDirectExchange";
    public static final String ROUTING_KEY_SOUND_TRAIN = "SoundTrainDirectRouting";

    // PPT总结队列
    public static final String QUEUE_NAME_PPT_SUMMARY = "PPTSummaryDirectQueue";
    public static final String EXCHANGE_NAME_PPT_SUMMARY = "PPTSummaryDirectExchange";
    public static final String ROUTING_KEY_PPT_SUMMARY = "PPTSummaryDirectRouting";

    // 任务返回结果队列
    public static final String QUEUE_NAME_TASK_BACK = "TaskBack";
    public static final String EXCHANGE_NAME_TASK_BACK = "TaskBackDirectExchange";
    public static final String ROUTING_KEY_TASK_BACK = "TaskBackDirectRouting";
    /**
     * 创建队列
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    public Queue SoundTrainDirectQueue() {
        /*
         * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
         * exclusive:是否独占,默认是false,只能被当前创建的连接使用，而且当连接关闭后队列即被删除。
         * autoDelete:是否自动删除,默认是false,当没有生产者或者消费者使用此队列，该队列会自动删除。
         */
        return new Queue(QUEUE_NAME_SOUND_TRAIN,true);
    }

    /**
     * 创建PPT总结队列
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    public Queue PPTDirectQueue() {
        return new Queue(QUEUE_NAME_PPT_SUMMARY,true);
    }

    /**
     * 创建任务返回结果队列
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    public Queue TaskBackQueue() {
        return new Queue(QUEUE_NAME_TASK_BACK,true);
    }

    /**
     * 创声音训练Direct交换机
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    DirectExchange SoundTrainDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME_SOUND_TRAIN,true,false);
    }

    /**
     * 创建PPT总结交换机
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    DirectExchange PPTDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME_PPT_SUMMARY,true,false);
    }
    /**
     * 创建任务返回结果交换机
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    DirectExchange TaskBackExchange() {
        return new DirectExchange(EXCHANGE_NAME_TASK_BACK,true,false);
    }

    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：SoundTrainDirectRouting
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    Binding bindingDirectSoundTrain() {
        return BindingBuilder.bind(SoundTrainDirectQueue()).to(SoundTrainDirectExchange()).with(ROUTING_KEY_SOUND_TRAIN);
    }

    /**
     * PPT总结绑定
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    Binding bindingDirectPPT() {
        return BindingBuilder.bind(PPTDirectQueue()).to(PPTDirectExchange()).with(ROUTING_KEY_PPT_SUMMARY);
    }

    /**
     * 任务返回结果绑定
     * @PARAM:
     * @RETURN: @return
     **/
    @Bean
    Binding bindingDirectBack() {
        return BindingBuilder.bind(TaskBackQueue()).to(TaskBackExchange()).with(ROUTING_KEY_TASK_BACK);
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }
}