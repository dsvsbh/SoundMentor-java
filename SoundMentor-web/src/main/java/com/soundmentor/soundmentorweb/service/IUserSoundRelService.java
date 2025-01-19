package com.soundmentor.soundmentorweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;

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

    /**
     * 根据用户文件路径获取声音
     * @param soundUrl
     * @PARAM: @param id
     * @RETURN: @return <p>
     **/
    UserSoundRelDO getByPath(Integer id, String soundUrl);

    /**
     * 获取用户自定义训练声音
     * @PARAM: @param id
     * @RETURN: @return
     **/
    List<UserSoundLibDTO> getUserTrainSoundLib(Integer id);
}
