package com.soundmentor.soundmentorweb.biz;

import cn.hutool.core.util.StrUtil;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AESUtil;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorbase.utils.JwtUtil;
import com.soundmentor.soundmentorbase.utils.MailUtil;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UserLoginParamByPassword;
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
    public Integer addUser(AddUserParam param) throws Exception {
        Boolean verifyTrue = verifyEmail(param.getEmail(), param.getCode());
        AssertUtil.isTrue(verifyTrue, "验证码错误");
        UserDO userDO = userParamConvert.convert(param);
        // 密码加密
        String password = AESUtil.encrypt(userDO.getPassword(), SoundMentorConstant.AES_KEY);
        userDO.setPassword(password);
        userDO.setCreatedTime(LocalDateTime.now());
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
    public Boolean sendEmail(String email) throws MessagingException {
        Integer code = MailUtil.achieveCode();
        MailUtil.sendTestMail(email, code);
        redisTemplate.opsForValue().set(StrUtil.format(SoundMentorConstant.REDIS_EAMIL_VERIFY_KEY,email), code,60, TimeUnit.SECONDS);
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
        try{
            String password = AESUtil.encrypt(param.getPassword(), SoundMentorConstant.AES_KEY);
            AssertUtil.isTrue(password.equals(userDO.getPassword()), "密码错误");
        }catch (Exception e){
            throw new BizException("密码解析错误");
        }
        String token = JwtUtil.generateToken(userDO.getUsername(),userDO.getId().toString());
        UserDTO userDTO = userParamConvert.convert(userDO);
        userDTO.setToken(token);
        redisTemplate.opsForValue().set(StrUtil.format(SoundMentorConstant.REDIS_USER_KEY,userDO.getId()), userDO, 120, TimeUnit.MINUTES);
        return userDTO;
    }

    public Void logout() {
        redisTemplate.delete(StrUtil.format(SoundMentorConstant.REDIS_USER_KEY,userInfoApi.getUser().getId()));
        userInfoApi.removeUser();
        return null;
    }
}
