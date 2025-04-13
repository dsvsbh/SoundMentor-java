package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.IdParam;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTTaskResultDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.biz.TaskBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


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
    @GetMapping(GET_TASK_BY_ID+"/{taskId}")
    public ResponseDTO<TaskMessageDTO<String>> getTaskById(@PathVariable Integer taskId)
    {
        return ResponseDTO.OK(taskBiz.getTaskById(taskId));
    }

    /**
     *  ppt任务轮询结果接口
     * @param userPptId
     * @return
     */
    @GetMapping("/getPptTask/{userPptId}")
    public ResponseDTO<List<PPTTaskResultDTO>> getPptTask(@PathVariable Integer userPptId)
    {
        return ResponseDTO.OK(taskBiz.getPptTask(userPptId));
    }

    /**
     * 用户编辑ppt任务的结果(讲解，音频)
     * @param list
     * @return
     */
    @PutMapping("/updatePPTResult")
    public ResponseDTO updatePPTResult(@RequestBody List<PPTTaskResultDTO> list)
    {
        taskBiz.updatePPTResult(list);
        return ResponseDTO.OK();
    }

    /**
     * 获取有声ppt todo 后续这里要用spring的异步驱动解耦，同步接口返回太慢
     * @param userPptId
     * @return 生成的有声ppt的url
     */
    @GetMapping("/getppt/{userPptId}")
    public ResponseDTO<String> getPpt(@PathVariable Integer userPptId)
    {
        return ResponseDTO.OK(taskBiz.getPpt(userPptId));
    }
}
