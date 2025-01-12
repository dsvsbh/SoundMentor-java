package com.soundmentor.soundmentorweb.biz;

import cn.hutool.core.lang.UUID;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundRelDTO;
import com.soundmentor.soundmentorweb.MQ.Producer.MqProducer;
import com.soundmentor.soundmentorweb.biz.convert.UserParamConvert;
import com.soundmentor.soundmentorweb.config.properties.UserProperties;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Integer addSound(String soundUrl) {
        AssertUtil.isTrue(canAddSound(), ResultCodeEnum.INTERNAL_ERROR.getCode(),"声音库数量已达到最大限制！");
        UserSoundRelDO addDO = new UserSoundRelDO();
        addDO.setSoundUrl(soundUrl);
        addDO.setUserId(userInfoApi.getUser().getId());
        addDO.setCreateTime(LocalDateTime.now());
        addDO.setStatus(0);
        Boolean res = userSoundRelService.addSound(addDO);
        if(res){
            String messageId = String.valueOf(UUID.randomUUID());
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", messageId);
            map.put("messageData", addDO);
            map.put("createTime", createTime);
            mqProducer.send("SoundTrainDirectExchange", "SoundTrainDirectRouting", map);
            log.info("消息ID:{},发送成功！",messageId);
        }
        return addDO.getId();
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
}
