package com.soundmentor.soundmentorweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.PPTXUtil;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserPptDetailDO;
import com.soundmentor.soundmentorpojo.DO.UserPptRelDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTPageSummaryTaskDTO;
import com.soundmentor.soundmentorpojo.DTO.ppt.PPTSummaryVoiceMsgDTO;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryTaskParam;
import com.soundmentor.soundmentorpojo.DTO.task.CreatePPTSummaryVoiceParam;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorweb.common.mq.producer.MqProducer;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptDetailMapper;
import com.soundmentor.soundmentorweb.mapper.UserPptRelMapper;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import com.soundmentor.soundmentorweb.service.PPTService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    @Resource
    private IUserSoundRelService userSoundRelService;
    @Resource
    private FileService fileService;

    /**
     * 创建ppt讲解生成任务
     * @param param
     * @return
     */
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
     * 创建ppt讲解语音生成任务
     * @param param
     * @return
     */
    @Override
    public Integer createPPTSummaryVoice(CreatePPTSummaryVoiceParam param) {
        UserSoundRelDO sound = userSoundRelService.getById(param.getUserSoundId());
        if(Objects.isNull(sound))
        {
            throw  new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"声音库不存在");
        }
        if(!sound.getUserId().equals(0))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"目前服务器暂不支持自定义声音库");
        }
        String apiParam = sound.getApiParam();
        List<UserPptDetailDO> userPptDetailDOS = userPptDetailMapper.selectList(new LambdaQueryWrapper<UserPptDetailDO>().eq(UserPptDetailDO::getUserPptId, param.getUserPptId()));
        for (UserPptDetailDO userPptDetailDO : userPptDetailDOS) {
            threadPoolExecutor.execute(()->{
                try {
                    taskExec(userPptDetailDO,apiParam,param.getRate());
                } catch (Exception e) {
                   log.info("ppt{}的{}页任务执行失败,请重试",userPptDetailDO.getUserPptId(),userPptDetailDO.getPptPage());
                }
            });
        }
        return param.getUserPptId();
    }

    /**
     * ppt页生成讲解任务执行
     * @param userPptId ppt标识
     * @param page 页码
     * @param slide ppt页对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void taskExec(Integer userPptId, Integer page,XSLFSlide slide) throws Exception
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
            //上传并添加预览图
            InputStream inputStream = PPTXUtil.convertSlideToImage(slide);
            String imgUrl = fileService.uploadFileToMinio(inputStream, FileTypeEnum.PNG, UUID.randomUUID().toString());
            userPptDetailDO.setImgUrl(imgUrl);
            userPptDetailMapper.insert(userPptDetailDO);
        } else {
            userPptDetailDO.setLastTaskId(taskDO.getId());
            userPptDetailMapper.updateById(userPptDetailDO);
        }
    }

    /**
     * ppt页生成讲解语音任务执行
     * @param userPptDetailDO
     * @param apiParam
     * @param rate
     */
    @Transactional(rollbackFor = Exception.class)
    public void taskExec(UserPptDetailDO userPptDetailDO,String apiParam, Integer rate)
    {
        String summary = userPptDetailDO.getSummary();
        if(StringUtils.isEmpty(summary))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"ppt"+userPptDetailDO.getUserPptId()+"的"+userPptDetailDO.getPptPage()+"页没有讲解");
        }
        PPTSummaryVoiceMsgDTO pptSummaryVoiceMsgDTO = new PPTSummaryVoiceMsgDTO();
        pptSummaryVoiceMsgDTO.setUserPptId(userPptDetailDO.getUserPptId());
        pptSummaryVoiceMsgDTO.setPage(userPptDetailDO.getPptPage());
        pptSummaryVoiceMsgDTO.setText(summary);
        pptSummaryVoiceMsgDTO.setVoiceName(apiParam);
        pptSummaryVoiceMsgDTO.setRate(rate);
        TaskDO taskDO = new TaskDO();
        taskDO.setType(TaskTypeEnum.PPT_SUMMARY_VOICE.getCode());
        taskDO.setStatus(TaskStatusEnum.CREATED.getCode());
        taskDO.setTaskDetail(JSON.toJSONString(pptSummaryVoiceMsgDTO));
        taskDO.setCreateTime(LocalDateTime.now());
        taskDO.setUpdateTime(LocalDateTime.now());
        taskMapper.insert(taskDO);
        userPptDetailDO.setLastTaskId(taskDO.getId());
        userPptDetailMapper.updateById(userPptDetailDO);
        TaskMessageDTO<PPTSummaryVoiceMsgDTO> message = new TaskMessageDTO<>();
        message.setId(taskDO.getId());
        message.setType(TaskTypeEnum.PPT_SUMMARY_VOICE.getCode());
        message.setStatus(TaskStatusEnum.CREATED.getCode());
        message.setMessageBody(pptSummaryVoiceMsgDTO);
        message.setCreateTime(LocalDateTime.now());
        mqProducer.send(DirectRabbitConfig.EXCHANGE_NAME_PPT_SUMMARY_VOICE, DirectRabbitConfig.ROUTING_KEY_PPT_SUMMARY_VOICE,message);
        log.info("消息ID:{},发送成功！----------->> {}",taskDO.getId(),message.toString());
    }
}
