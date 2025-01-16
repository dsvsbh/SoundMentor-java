package com.soundmentor.soundmentorweb.biz;

import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.enums.TaskStatusEnum;
import com.soundmentor.soundmentorbase.enums.TaskTypeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DO.TaskDO;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.task.TaskMessageDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundRelDTO;
import com.soundmentor.soundmentorweb.common.mq.producer.MqProducer;
import com.soundmentor.soundmentorweb.biz.convert.UserParamConvert;
import com.soundmentor.soundmentorweb.config.mqConfig.DirectRabbitConfig;
import com.soundmentor.soundmentorweb.config.properties.UserProperties;
import com.soundmentor.soundmentorweb.mapper.TaskMapper;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 声音样本库相关业务逻辑
 * @Author: Make
 * @DATE: 2025/01/11
 **/
@Component
@Slf4j
public class UserSoundBiz {
    @Resource
    private IUserSoundRelService userSoundRelService;
    @Resource
    private UserProperties userProperties;
    @Resource
    private UserParamConvert userParamConvert;
    @Resource
    private UserInfoApi userInfoApi;
    @Resource
    private MqProducer mqProducer;
    @Resource
    private TaskMapper taskMapper;
    /**
     * 是否能继续添加声音到声音样本库
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean canAddSound() {
        UserDO userDO = userInfoApi.getUser();
        List<UserSoundRelDO> soundList = userSoundRelService.getSoundByUserId(userDO.getId());
        Integer num = 0;
        if(soundList != null){
            num = soundList.size();
        }
        return num <= userProperties.getMaxSound();
    }

    /**
     * 添加声音进行训练
     * @PARAM: @param data
     * @RETURN: @return
     **/
    @Transactional(rollbackFor = Exception.class)
    public Integer addSound(String soundUrl) {
        AssertUtil.isTrue(canAddSound(), ResultCodeEnum.INTERNAL_ERROR.getCode(),"声音库数量已达到最大限制！");
        try{
            if(!FileTypeEnum.MP3.getSuffix().equals(soundUrl.substring(soundUrl.lastIndexOf("."))))
            {
                throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "请上传mp3格式文件");
            }
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "请上传mp3格式文件");
        }
        UserSoundRelDO userSoundRelDO = userSoundRelService.getByPath(userInfoApi.getUser().getId(),soundUrl);
        if(userSoundRelDO==null)
        {
            userSoundRelDO = new UserSoundRelDO();
            userSoundRelDO.setSoundUrl(soundUrl);
            userSoundRelDO.setUserId(userInfoApi.getUser().getId());
            userSoundRelDO.setCreateTime(LocalDateTime.now());
            userSoundRelDO.setStatus(TaskStatusEnum.CREATED.getCode());
            userSoundRelService.addSound(userSoundRelDO);
        }
        TaskDO taskDO = new TaskDO();
        taskDO.setTaskDetail(JSON.toJSONString(userSoundRelDO));
        taskDO.setStatus(TaskStatusEnum.CREATED.getCode());
        taskDO.setType(TaskTypeEnum.VOICE_TRAIN.getCode());
        taskDO.setCreateTime(LocalDateTime.now());
        taskDO.setUpdateTime(LocalDateTime.now());
        taskMapper.insert(taskDO);
        TaskMessageDTO<UserSoundRelDO> message = new TaskMessageDTO<>();
        message.setId(taskDO.getId());
        message.setType(TaskTypeEnum.VOICE_TRAIN.getCode());
        message.setMessageBody(userSoundRelDO);
        message.setStatus(TaskStatusEnum.CREATED.getCode());
        message.setCreateTime(LocalDateTime.now());
        mqProducer.send(DirectRabbitConfig.EXCHANGE_NAME_SOUND_TRAIN, DirectRabbitConfig.ROUTING_KEY_SOUND_TRAIN,message);
        /// mqProducer.send(DirectRabbitConfig.EXCHANGE_NAME_TASK_BACK, DirectRabbitConfig.ROUTING_KEY_TASK_BACK,message);
        log.info("消息ID:{},发送成功！----------->> {}",taskDO.getId(),message.toString());
        return taskDO.getId();
    }

    /**
     * 获取声音
     * @PARAM: @param id
     * @RETURN: @return
     **/
    public UserSoundRelDTO getSound(Integer id) {
        UserSoundRelDO userSoundRelDO = userSoundRelService.getById(id);
        AssertUtil.notNull(userSoundRelDO, ResultCodeEnum.DATA_NOT_FUND.getCode(),"声音不存在");
        AssertUtil.isTrue(userSoundRelDO.getUserId().equals(userInfoApi.getUser().getId()),
                ResultCodeEnum.DATA_DENIED.getCode(),"您无数据权限");
        UserSoundRelDTO res = userParamConvert.convert(userSoundRelDO);
        return res;
    }

    /**
     * 删除声音
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean delSoundList(List<Integer> ids) {
        // 先获取用户文件path
        List<UserSoundRelDO> soundList = userSoundRelService.getSoundByIds(ids);
        List<String> filePaths = soundList.stream().map(UserSoundRelDO::getSoundUrl).collect(Collectors.toList());
        // todo:删除文件列表

        return userSoundRelService.delSoundList(ids);
    }

    /**
     * 获取用户声音列表
     * @PARAM:
     * @RETURN: @return
     **/
    public List<UserSoundRelDTO> getSoundList() {
        List<UserSoundRelDO> soundList = userSoundRelService.getSoundByUserId(userInfoApi.getUser().getId());
        AssertUtil.ifNull(soundList, ResultCodeEnum.DATA_NOT_FUND.getCode(),"声音不存在");
        return soundList.stream().map(userParamConvert::convert).collect(Collectors.toList());
    }

    /**
     * 更新声音
     * @PARAM: @param updateDO
     * @RETURN: @return
     **/
    public Boolean updateSound(UserSoundRelDO updateDO){
        return userSoundRelService.updateSound(updateDO);
    }
}
