package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.IdParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.biz.TaskBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 任务相关接口
 * @author liuzhicheng
 * @since 2025-01-12
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final static String GET_TASK_BY_ID = "/getTaskById";
    private final static String CREATE_TASK = "/createTask";
    @Resource
    private final TaskBiz taskBiz;

    /**
     * 任务执行公共接口
     * @param param
     * @return
     */
    @PostMapping(CREATE_TASK)
    public ResponseDTO<Integer> create(@RequestBody CreateTaskParam param)
    {
        return ResponseDTO.OK(taskBiz.createTask(param));
    }

    /**
     * 获取任务详情
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(GET_TASK_BY_ID)
    public ResponseDTO<TaskMessageDTO<String>> getTaskById(@Valid @RequestBody IdParam param)
    {
        return ResponseDTO.OK(taskBiz.getTaskById(param.getId()));
    }
}
