package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.StringParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UpdateUserInfoParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UpdateUserPasswordParam;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
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
    public static final String LOGOUT = "/logout";
    public static final String GET_WEB_USER = "/getWebUser";
    public static final String UPDATE_PASSWORD = "/updatePassword";
    public static final String UPDATE_USER_INFO = "/updateUserInfo";
    @Resource
    private UserBiz userBiz;

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
    @GetMapping(GET_WEB_USER)
    public ResponseDTO<UserDTO> getUserInfo(){
        return ResponseDTO.OK(userBiz.getWebUser());
    }

    /**
     * 密码修改
     * @PARAM: @param password
     * @RETURN: @return
     **/
    @PostMapping(UPDATE_PASSWORD)
    public ResponseDTO<Boolean> updatePassword(@Valid @RequestBody UpdateUserPasswordParam param){
        return ResponseDTO.OK(userBiz.updatePassword(param));
    }

    /**
     * 用户个人资料修改
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(UPDATE_USER_INFO)
    public ResponseDTO<Boolean> updateUserInfo(@Valid @RequestBody UpdateUserInfoParam param){
        return ResponseDTO.OK(userBiz.updateUser(param));
    }

}
