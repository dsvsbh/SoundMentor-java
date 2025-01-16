package com.soundmentor.soundmentorweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.utils.PPTXUtil;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserPptDetailDO;
import com.soundmentor.soundmentorpojo.DO.UserPptRelDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTPageSummaryTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.mq.producer.MqProducer;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptDetailMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptRelMapper;
import com.soundmentor.soundmentorweb.service.PPTService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j

public class PPTServiceImpl implements PPTService {
    @Resource
    private  UserPptDetailMapper userPptDetailMapper;
    @Resource
    private  UserPptRelMapper userPptRelMapper;
    @Resource
    private  UserInfoApi userInfoApi;
    @Resource(name = "task-thread-pool-executor")
    private  ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private TaskMapper taskMapper;
    @Resource
    private MqProducer mqProducer;

    @Override
    public Integer createPPTSummary(CreatePPTSummaryTaskParam param) {
        String pptUrl = param.getPptUrl();
        XMLSlideShow xmlSlideShow = PPTXUtil.loadPPTX(pptUrl);
        List<XSLFSlide> slides = xmlSlideShow.getSlides();
        UserPptRelDO userPptRelDO = userPptRelMapper.selectOne(new LambdaQueryWrapper<UserPptRelDO>()
                .eq(UserPptRelDO::getPptUrl, pptUrl)
                .eq(UserPptRelDO::getUserId, userInfoApi.getUser().getId()));
        if (Objects.isNull(userPptRelDO))
        {
            userPptRelDO = new UserPptRelDO();
            userPptRelDO.setPptUrl(pptUrl);
            userPptRelDO.setUserId(userInfoApi.getUser().getId());
            userPptRelDO.setPageCount(slides.size());
            userPptRelDO.setCreateTime(LocalDateTime.now());
            userPptRelMapper.insert(userPptRelDO);
        }
        for (int i = 0; i < slides.size(); i++) {
            Integer userPptId = userPptRelDO.getId();
            Integer page = i;
            XSLFSlide slide = slides.get(page);
            threadPoolExecutor.execute(()->{
                try {
                    taskExec(userPptId,page,slide);
                } catch (Exception e) {
                    log.error("ppt{}的{}页任务执行失败,请重试",userPptId,page);
                }
            });
        }
        return userPptRelDO.getId();
    }

    /**
     * ppt页生成讲解任务执行
     * @param userPptId ppt标识
     * @param page 页码
     * @param slide ppt页对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void taskExec(Integer userPptId, Integer page,XSLFSlide slide)
    {
        TaskDO taskDO = new TaskDO();
        PPTPageSummaryTaskDTO pptPageSummaryTaskDTO = new PPTPageSummaryTaskDTO();
        pptPageSummaryTaskDTO.setUserPptId(userPptId);
        pptPageSummaryTaskDTO.setPage(page);
        pptPageSummaryTaskDTO.setContent(PPTXUtil.getSlideInfo(slide));
        taskDO.setTaskDetail(JSON.toJSONString(pptPageSummaryTaskDTO));
        taskDO.setType(TaskTypeEnum.PPT_SUMMARY.getCode());
        taskDO.setUpdateTime(LocalDateTime.now());
        taskDO.setCreateTime(LocalDateTime.now());
        taskDO.setStatus(TaskStatusEnum.CREATED.getCode());
        taskMapper.insert(taskDO);
        TaskMessageDTO<PPTPageSummaryTaskDTO> taskMessage = new TaskMessageDTO<>();
        taskMessage.setId(taskDO.getId());
        taskMessage.setType(TaskTypeEnum.PPT_SUMMARY.getCode());
        taskMessage.setStatus(TaskStatusEnum.CREATED.getCode());
        taskMessage.setMessageBody(pptPageSummaryTaskDTO);
        taskMessage.setCreateTime(LocalDateTime.now());
        mqProducer.send(DirectRabbitConfig.EXCHANGE_NAME_PPT_SUMMARY, DirectRabbitConfig.ROUTING_KEY_PPT_SUMMARY,taskMessage);
        UserPptDetailDO userPptDetailDO = userPptDetailMapper.selectOne(new LambdaQueryWrapper<UserPptDetailDO>()
                .eq(UserPptDetailDO::getUserPptId, userPptId)
                .eq(UserPptDetailDO::getPptPage, page));
        if(Objects.isNull(userPptDetailDO))
        {
            userPptDetailDO = new UserPptDetailDO();
            userPptDetailDO.setUserPptId(userPptId);
            userPptDetailDO.setPptPage(page);
            userPptDetailDO.setCreateTime(LocalDateTime.now());
            userPptDetailDO.setLastTaskId(taskDO.getId());
            userPptDetailMapper.insert(userPptDetailDO);
        } else {
            userPptDetailDO.setLastTaskId(taskDO.getId());
            userPptDetailMapper.updateById(userPptDetailDO);
        }
    }
}
