package com.soundmentor.soundmentorweb.controller.openApi;

import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.ForgetPasswordParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UserLoginParamByPassword;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorweb.annotation.RequestDuplicationCondition;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import com.soundmentor.soundmentorweb.biz.util.limit.WebUserKey;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;

/**
 * 用户相关免登接口
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@RestController
@RequestMapping("/openApi/user")
public class UserOpenController {
    public static final String ADD_USER = "/addUser";
    public static final String LOGIN = "/login";
    public static final String SEND_EMAIL = "/sendEmail";
    public static final String FORGET_PASSWORD = "/forgetPassword";
    @Resource
    private UserBiz userBiz;

    /**
     * 发送邮件
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping( SEND_EMAIL)
    @RequestDuplicationCondition(keyPrefix = "EMAIL:SEND:", key = "",
            keyReadClass = WebUserKey.class, requestIntervalMs = 30000L)
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
     * 忘记密码
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(FORGET_PASSWORD)
    public ResponseDTO<Boolean> forgetPassword(@Valid @RequestBody ForgetPasswordParam param){
        return ResponseDTO.OK(userBiz.fogetPassword(param));
    }
}
