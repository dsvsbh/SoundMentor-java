package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorpojo.DO.TaskDO;

import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务表，存储所有任务的详细信息和状态 服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskDO> implements ITaskService {

}
