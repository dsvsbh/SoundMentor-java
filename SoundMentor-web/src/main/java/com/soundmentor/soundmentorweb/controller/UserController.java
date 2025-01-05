package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorweb.biz.UserBiz;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * 用户相关接口
 * @author Make
 * @since 2025-01-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final String SEND_EMAIL = "sendEmail";
    @Resource
    private UserBiz userBiz;

    /**
     * 发送邮件【开发中】
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping( SEND_EMAIL)
    public ResponseDTO<Integer> sendEmail() {
        return ResponseDTO.ok(userBiz.sendEmail());
    }
}
