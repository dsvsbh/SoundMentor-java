//package com.soundmentor.soundmentorweb.MQ.Consumer;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
///**
// * 监听所有任务结果
// */
//@Component
//public class TaskMessageReceiver {
//    @RabbitListener(queues = "TaskBack")//监听的队列名称 TestDirectQueue
//    public void process(String taskResult) {
//
//    }
//}