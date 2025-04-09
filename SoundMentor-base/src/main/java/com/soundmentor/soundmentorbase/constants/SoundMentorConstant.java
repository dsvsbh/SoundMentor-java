package com.soundmentor.soundmentorbase.constants;

/**
 * 项目常量类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
public final class SoundMentorConstant {
    private SoundMentorConstant(){}

    /**
     * minio mp3前缀
     **/
    public static final String MINIO_MP3_PREFIX = "http://8.148.229.137:9000/mp3/";
    /**
     * 验证码redis key
     **/
    public static final String REDIS_EAMIL_VERIFY_KEY = "email:verify:{}";
    /**
      redis用户 key
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

    public static final String MAIL_CONTENT = "<html><body>主题: 您的验证码<br>尊敬的用户:<br>您好! <br>" +
            "<img src=\"https://s2.loli.net/2025/01/09/UNMOVLJAFs7YCBk.png\" alt=\"公司logo\" style=\"display:block; margin:0 auto; max-width:100px; height:auto;\"><br>" +
            "非常感谢您使用我们的服务。为了确保您账户的安全，本次操作需要验证码进行验证。" +
            "您的验证码是: {}，该验证码有效期为 2 分钟，请及时输入，切勿向他人透露。<br>" +
            "如果您并未进行此操作，请忽略本邮件，同时建议您及时检查账户安全设置，如有任何疑问，请随时联系我们的客服团队，" +
            "客服电话: 123456789。<br>再次感谢您对我们的信任与支持，祝您生活愉快! <br>" +
            "[Sound-Mentor] 团队<br>{}</body></html>";

    /**
     * 默认头像地址
     */
    public static final String DEFAULT_HEAD_IMG = "default";//todo 待定
    /**
     * token过期时间,单位为分钟
     **/
    public static final Integer TOKEN_EXPIRE_TIME = 120;

    /**
     * 验证码过期时间,单位为分钟
     **/
    public static final Integer VERIFY_CODE_EXPIRE_TIME = 2;

    /**
     * 防止重复提交key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat:submit:";

    /**
     * 组织分享码key
     */
    public static final String ORGANIZATION_SHARE_CODE_KEY="organization:share:code:";
    /**
     * 组织加入锁key
     */
    public static final String ORGANIZATION_JOIN_LOCK_KEY ="organization:join:lock:";
}
