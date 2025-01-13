package com.soundmentor.soundmentorweb.factory;

import com.soundmentor.soundmentorweb.handler.TaskHandler;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TaskHandlerFactory {
    private static final ConcurrentHashMap<Integer, TaskHandler> map = new ConcurrentHashMap<>();
    public static void register(TaskHandler taskHandler) {
        map.put(taskHandler.getTaskType(), taskHandler);
    }
    public TaskHandler getTaskHandler(Integer taskType) {
        return map.get(taskType);
    }
}
