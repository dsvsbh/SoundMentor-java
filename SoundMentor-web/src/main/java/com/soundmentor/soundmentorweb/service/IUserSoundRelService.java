package com.soundmentor.soundmentorweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;

import java.util.List;

/**
 * <p>
 * 用于存储用户声音相关记录的表 服务类
 * </p>
 *
 * @author Make
 * @since 2025-01-11
 */
public interface IUserSoundRelService extends IService<UserSoundRelDO> {

    /**
     * 根据用户id获取用户声音
     * @PARAM: @param id
     * @RETURN: @return
     **/
    List<UserSoundRelDO> getSoundByUserId(Integer userId);

    /**
     * 添加声音
     * @PARAM: @param addDO
     * @RETURN: @return
     **/
    Boolean addSound(UserSoundRelDO addDO);

    /**
     * 根据id获取声音
     * @PARAM: @param ids
     * @RETURN: @return
     **/
    List<UserSoundRelDO> getSoundByIds(List<Integer> ids);

    /**
     * 根据id删除声音
     * @PARAM: @param ids
     * @RETURN: @return
     **/
    Boolean delSoundList(List<Integer> ids);

    /**
     * 根据id更新声音
     * @PARAM: @param updateDO
     * @RETURN: @return
     **/
    Boolean updateSound(UserSoundRelDO updateDO);
}
