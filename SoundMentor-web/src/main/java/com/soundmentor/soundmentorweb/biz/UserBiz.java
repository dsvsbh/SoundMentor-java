package com.soundmentor.soundmentorweb.biz;

import cn.hutool.core.util.StrUtil;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AESUtil;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorbase.utils.JwtUtil;
import com.soundmentor.soundmentorbase.utils.MailUtil;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.user.req.*;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorweb.biz.convert.UserParamConvert;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import com.soundmentor.soundmentorweb.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserParamConvert userParamConvert;
    @Resource
    private UserInfoApi userInfoApi;

    /**
     * 新增用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    public Integer addUser(AddUserParam param){
        AssertUtil.isTrue(isValidEmail(param.getEmail()), "邮箱格式错误");
        Boolean verifyTrue = verifyEmail(param.getEmail(), param.getVerifyCode());
        AssertUtil.isTrue(verifyTrue, "验证码错误");
        UserDO userDO = userParamConvert.convert(param);
        // 密码加密
        String password = AESUtil.encrypt(userDO.getPassword(), SoundMentorConstant.AES_KEY);
        userDO.setPassword(password);
        userDO.setCreatedTime(LocalDateTime.now());
        userDO.setHeadImg(SoundMentorConstant.DEFAULT_HEAD_IMG);
        return userService.addUser(userDO);
    }
    /**
     * 更新用户
     * @PARAM: @param userDO
     * @RETURN: @return
     **/
    public Boolean updateUser(UpdateUserInfoParam param) {
        UserDO userDO = userParamConvert.convert(param);
        UserDO user = userInfoApi.getUser();
        userDO.setId(user.getId());
        return userService.updateUser(userDO);
    }

    /**
     * 发送邮件
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean sendEmail(String email){
        AssertUtil.isTrue(isValidEmail(email), "邮箱格式错误");
        // redis里面有不重复发送验证码
        Integer redisCode = (Integer) redisTemplate.opsForValue().get(
                StrUtil.format(SoundMentorConstant.REDIS_EAMIL_VERIFY_KEY,email));
        if(redisCode != null){
            return true;
        }
        // 否则发送验证码
        Integer code = MailUtil.achieveCode();
        try{
            MailUtil.sendTestMail(email, code);
            redisTemplate.opsForValue().set(StrUtil.format(SoundMentorConstant.REDIS_EAMIL_VERIFY_KEY,email)
                    , code,SoundMentorConstant.VERIFY_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        }catch (MessagingException e){
            log.error("发送邮件失败",e);
            throw new BizException("发送邮件失败");
        }
        return true;
    }

    /**
     * 验证邮件
     * @param code
     * @PARAM: @param email
     * @RETURN: @return
     **/
    public Boolean verifyEmail(String email, Integer code){
        Integer redisCode = (Integer) redisTemplate.opsForValue().get(StrUtil.format(SoundMentorConstant.REDIS_EAMIL_VERIFY_KEY,email));
        if(redisCode == null){
            return false;
        }
        return redisCode.equals(code);
    }

    /**
     * 登录
     * @PARAM: @param param
     * @RETURN: @return
     **/
    public UserDTO login(UserLoginParamByPassword param) {
        UserDO userDO = userService.getByUserName(param.getUsername());
        AssertUtil.ifNull(userDO, "用户不存在");

        // 密码解析
        String password = AESUtil.encrypt(param.getPassword(), SoundMentorConstant.AES_KEY);
        AssertUtil.isTrue(password.equals(userDO.getPassword()), "密码错误");

        String token = JwtUtil.generateToken(userDO.getUsername(),userDO.getId().toString());
        UserDTO userDTO = userParamConvert.convert(userDO);
        userDTO.setToken(token);
        redisTemplate.opsForValue().set(StrUtil.format(SoundMentorConstant.REDIS_USER_KEY,userDO.getId()),
                userDO, SoundMentorConstant.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
        return userDTO;
    }

    /**
     * 退出登录
     * @PARAM:
     * @RETURN: @return
     **/
    public Boolean logout() {
        redisTemplate.delete(StrUtil.format(SoundMentorConstant.REDIS_USER_KEY,userInfoApi.getUser().getId()));
        userInfoApi.removeUser();
        return true;
    }

    /**
     * 获取用户信息
     * @PARAM:
     * @RETURN: @return
     **/
    public UserDTO getWebUser() {
        UserDO userDO = userInfoApi.getUser();
        return userParamConvert.convert(userDO);
    }

    /**
     * 忘记密码
     * @PARAM: @param param
     * @RETURN: @return
     **/
    public Boolean fogetPassword(ForgetPasswordParam param) {
        Boolean verifyTrue = verifyEmail(param.getEmail(), param.getVerifyCode());
        AssertUtil.isTrue(verifyTrue, "验证码错误");
        AssertUtil.isTrue(isValidEmail(param.getEmail()), "邮箱格式错误");
        String password = AESUtil.encrypt(param.getPassword(), SoundMentorConstant.AES_KEY);
        return userService.updatePassword(param.getEmail(), password);
    }

    /**
     * 验证邮箱格式
     * @PARAM: @param email
     * @RETURN: @return boolean
     **/
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * 密码修改
     * @PARAM: @param param
     * @RETURN: @return
     **/
    public Boolean updatePassword(UpdateUserPasswordParam param) {
        UserDO userDO = userInfoApi.getUser();
        // 密码解析
        String password = AESUtil.encrypt(param.getOldPassword(), SoundMentorConstant.AES_KEY);
        AssertUtil.isTrue(password.equals(userDO.getPassword()), "旧密码错误");
        String newPassword = AESUtil.encrypt(param.getNewPassword(), SoundMentorConstant.AES_KEY);
        return userService.updatePassword(userDO.getEmail(), newPassword);
    }
}
