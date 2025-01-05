package com.soundmentor.soundmentorweb.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorweb.mapper.UserMapper;
import com.soundmentor.soundmentorweb.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

    /**
     * 新增用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    @Override
    public Integer addUser(UserDO userDO) {
        this.save(userDO);
        return userDO.getId();
    }

    /**
     * 修改用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    @Override
    public Boolean updateUser(UserDO userDO) {
        return this.updateUser(userDO);
    }

    /**
     * 根据用户名查询用户
     * @PARAM: @param username
     * @RETURN: @return <p>
     **/
    @Override
    public UserDO getByUserName(String username) {
        return this.getOne(Wrappers.<UserDO> lambdaQuery()
            .eq(UserDO::getUsername, username));
    }
}
