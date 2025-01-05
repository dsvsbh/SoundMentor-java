package com.soundmentor.soundmentorweb.biz;

import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorweb.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户相关业务逻辑
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Component
@Slf4j
public class UserBiz {
    @Resource
    private UserServiceImpl userService;

    /**
     * 新增用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    public Boolean addUser(UserDO userDO) {
        return userService.addUser(userDO);
    }
    /**
     * 更新用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    public Boolean updateUser(UserDO userDO) {
        return userService.updateUser(userDO);
    }

    /**
     * 发送邮件
     * @PARAM:
     * @RETURN: @return
     **/
    public Integer sendEmail() {
        //发送邮件
        //发送成功
        return new Integer(132123);
    }
}
