package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UserLoginParamByPassword;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;

/**
 *
 * 用户相关接口
 * @author Make
 * @since 2025-01-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    public static final String SEND_EMAIL = "/sendEmail";
    public static final String ADD_USER = "/addUser";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String GET_WEB_USER = "/getWebUser";

    @Resource
    private UserBiz userBiz;


    /**
     * 发送邮件
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping( SEND_EMAIL)
    public ResponseDTO<Boolean> sendEmail(@RequestParam("email") String email) throws MessagingException {
        AssertUtil.hasLength(email, "邮箱不能为空");
        return ResponseDTO.OK(userBiz.sendEmail(email));
    }

    /**
     * 用户注册
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(ADD_USER)
    public ResponseDTO<Integer> addUser(@Valid @RequestBody AddUserParam param) throws Exception {
        return ResponseDTO.OK(userBiz.addUser(param));
    }

    /**
     * 用户登录
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(LOGIN)
    public ResponseDTO<UserDTO> login(@Valid @RequestBody UserLoginParamByPassword param){
        return ResponseDTO.OK(userBiz.login(param));
    }

    /**
     * 用户登出
     * 通过token登出用户
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(LOGOUT)
    public ResponseDTO<Boolean> logout(){
        return ResponseDTO.OK(userBiz.logout());
    }
    /**
     * 获取用户信息
     */
    @PostMapping(GET_WEB_USER)
    public ResponseDTO<UserDTO> getUserInfo(){
        return ResponseDTO.OK(userBiz.getWebUser());
    }
}
