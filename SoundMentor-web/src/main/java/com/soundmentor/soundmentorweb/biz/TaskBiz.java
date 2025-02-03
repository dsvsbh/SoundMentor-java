package com.soundmentor.soundmentorweb.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserPptDetailDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTTaskResultDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreateTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.factory.TaskHandlerFactory;
import com.soundmentor.soundmentorweb.mapper.UserPptDetailMapper;
import com.soundmentor.soundmentorweb.service.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 任务相关业务逻辑
 * @Author: Make
 * @DATE: 2025/01/14
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskBiz {
    @Resource
    private ITaskService taskService;
    private final TaskHandlerFactory taskHandlerFactory;
    @Autowired
    private UserPptDetailMapper userPptDetailMapper;
    @Resource(name = "task-thread-pool-executor")
    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * 根据id获取任务信息
     * @PARAM: @param id
     * @RETURN: @return 返回结果统一封装
     **/
    public TaskMessageDTO<String> getTaskById(Integer id) {
        TaskDO taskDO = taskService.getById(id);
        AssertUtil.notNull(taskDO, ResultCodeEnum.DATA_NOT_FUND.getCode(),"任务不存在");
        TaskMessageDTO<String> res = new TaskMessageDTO();
        res.setId(taskDO.getId());
        res.setCreateTime(taskDO.getCreateTime());
        res.setStatus(taskDO.getStatus());
        res.setType(taskDO.getType());
        res.setMessageBody(taskDO.getResult());
        return res;
    }

    /**
     * 获取任务详情
     * @PARAM: @param id
     * @RETURN: @return
     **/
    public String getTaskDetail(Integer id) {
        TaskDO taskDO = taskService.getById(id);
        return taskDO.getTaskDetail();
    }

    /**
     * 创建任务
     * @PARAM: @param param
     * @RETURN: @return
     **/
    public Integer createTask(CreateTaskParam param) {
        return taskHandlerFactory.getTaskHandler(param.getTaskType().getCode()).createTask(param);
    }

    /**
     * 更新任务记录
     * @PARAM: @param taskMessage
     * @RETURN: @return
     **/
    public Boolean updateTask(TaskMessageDTO taskMessage) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(taskMessage.getId());
        taskDO.setStatus(taskMessage.getStatus());
        taskDO.setResult(taskMessage.getMessageBody().toString());
        taskDO.setUpdateTime(LocalDateTime.now());
        return taskService.updateById(taskDO);
    }

    public List<PPTTaskResultDTO> getPptTask(Integer userPptId) {
        List<UserPptDetailDO> userPptDetails = userPptDetailMapper.selectList(new LambdaQueryWrapper<UserPptDetailDO>()
                .eq(UserPptDetailDO::getUserPptId, userPptId)
                .orderByAsc(UserPptDetailDO::getPptPage));
        if(Objects.isNull(userPptDetails))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"该ppt不存在任务");
        }
        return userPptDetails.stream()
                .map(userPptDetailDO -> {
                    PPTTaskResultDTO pptTaskResultDTO = new PPTTaskResultDTO();
                    pptTaskResultDTO.setUserPptId(userPptId);
                    pptTaskResultDTO.setPptPage(userPptDetailDO.getPptPage());
                    pptTaskResultDTO.setSummary(userPptDetailDO.getSummary());
                    pptTaskResultDTO.setSoundUrl(userPptDetailDO.getSoundUrl());
                    pptTaskResultDTO.setImgUrl(userPptDetailDO.getImgUrl());
                    return pptTaskResultDTO;
                }).collect(Collectors.toList());
    }

    public void updatePPTResult(List<PPTTaskResultDTO> list) {
        for (PPTTaskResultDTO pptTaskResultDTO : list) {
            threadPoolExecutor.execute(() -> {
                try {
                    userPptDetailMapper.updateResult(pptTaskResultDTO);
                } catch (Exception e) {
                    log.error("更新ppt{}的{}页任务失败,请重试", pptTaskResultDTO.getUserPptId(), pptTaskResultDTO.getPptPage());
                }
            });
        }
    }
}
