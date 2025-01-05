package com.soundmentor.soundmentorbase.constant;

/**
 * 项目常量类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public final class SoundMentorConstant {
    private SoundMentorConstant(){}

    /**
     * 验证码redis key
     **/
    public static final String REDIS_EAMIL_VERIFY_KEY = "email:verify:{}";
    /**
      redis用户可以
     **/
    public static final String REDIS_USER_KEY = "userId:{}";
    /**
     * 加密算法秘钥
     **/
    public static final String AES_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6";
    /**
     * JWT秘钥
     **/
    public static final String SECRET = "JOE38R39GNGRTU49Y534YNIGEYR534YNDEUR7964GEUR735";

}
