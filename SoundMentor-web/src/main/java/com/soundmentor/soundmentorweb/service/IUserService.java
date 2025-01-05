package com.soundmentor.soundmentorweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.UserDO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
public interface IUserService extends IService<UserDO> {
    /**
     * 新增用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    Boolean addUser(UserDO userDO);

    /**
     * 修改用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    Boolean updateUser(UserDO userDO);
}
