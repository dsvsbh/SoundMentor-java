package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateVoiceTrainParam;
import com.soundmentor.soundmentorweb.factory.TaskHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

/**
 * 任务相关接口
 * @author liuzhicheng
 * @since 2025-01-12
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskHandlerFactory taskHandlerFactory;

    /**
     * 任务执行公共接口
     * @param param
     * @return
     */
    @PostMapping("/create")
    public ResponseDTO<Integer> create(@RequestBody CreateTaskParam param)
    {
        return ResponseDTO.OK(taskHandlerFactory.getTaskHandler(param.getTaskType().getCode()).createTask(param));
    }
}
