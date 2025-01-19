package com.soundmentor.soundmentorweb.common.aop.aspects;

import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.factory.TaskHandlerFactory;
import com.soundmentor.soundmentorweb.common.handler.TaskHandler;
import com.soundmentor.soundmentorweb.common.mq.producer.DelayTaskMessageSender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
public class TaskTimeLimitAspect {
    @Resource
    private TaskHandlerFactory taskHandlerFactory;
    @Resource
    private DelayTaskMessageSender delayTaskMessageSender;

    @Pointcut("execution(public void com.soundmentor.soundmentorweb.common.mq.producer.MqProducer.send(..))")
    public void taskTimeLimit() {
    }
    @After("taskTimeLimit()")
    public void after(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        TaskMessageDTO taskMessage = (TaskMessageDTO) args[2];
        TaskHandler taskHandler = taskHandlerFactory.getTaskHandler(taskMessage.getType());
        if (taskHandler != null && taskHandler.timeLimit() != null) {
            Integer timeLimit = taskHandler.timeLimit();
            if (timeLimit != null && timeLimit > 0) {
                delayTaskMessageSender.sendDelayMessage(taskMessage.getId().toString(), timeLimit);
           }
        }
    }
}
