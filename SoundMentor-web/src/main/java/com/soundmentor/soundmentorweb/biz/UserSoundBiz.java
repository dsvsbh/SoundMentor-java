package com.soundmentor.soundmentorweb.biz;

import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundRelDTO;
import com.soundmentor.soundmentorweb.biz.convert.UserParamConvert;
import com.soundmentor.soundmentorweb.config.properties.UserProperties;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
    /**
     * 是否能继续添加声音到声音样本库
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean canAddSound() {
        return null;
    }

    /**
     * 添加声音进行训练
     * @PARAM: @param data
     * @RETURN: @return
     **/
    public Boolean addSound(String data) {
        return null;
    }

    /**
     * 获取声音
     * @PARAM: @param id
     * @RETURN: @return
     **/
    public UserSoundRelDTO getSound(Integer id) {
        UserSoundRelDO userSoundRelDO = userSoundRelService.getById(id);
        AssertUtil.notNull(userSoundRelDO, ResultCodeEnum.DATA_NOT_FUND.getCode(),"声音不存在");
        UserSoundRelDTO res = userParamConvert.convert(userSoundRelDO);
        return res;
    }

    /**
     * 删除声音
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean delSoundList(List<Integer> ids) {
        return null;
    }

    /**
     * 获取用户声音列表
     * @PARAM:
     * @RETURN: @return
     **/
    public List<UserSoundRelDTO> getSoundList() {
        return null;
    }
}
