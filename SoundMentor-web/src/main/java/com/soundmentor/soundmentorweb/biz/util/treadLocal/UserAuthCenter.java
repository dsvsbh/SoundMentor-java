package com.soundmentor.soundmentorweb.biz.util.treadLocal;

import com.soundmentor.soundmentorpojo.DO.UserDO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthCenter {
    public static final String TOKEN_HEADER = "Authorization";
    /**
     * 保存了token信息
     *
     * @date 2022/12/06
     * @see ThreadLocal<String>
     */
    private static ThreadLocal<String> TOKEN = new ThreadLocal<>();
    /**
     * 存储当前用户信息
     */
    private static ThreadLocal<UserDO> USER = new ThreadLocal<>();

    /**
     * 获取当前线程的 token信息
     *
     * @return {@link String}
     */
    public static String getToken() {
        return TOKEN.get();
    }

    /**
     * 设置获取当前线程的token信息
     *
     * @param token 令牌
     */
    public static void setToken(String token) {
        TOKEN.set(token);
    }

    /**
     * 设置当前线程的web用户
     *
     * @param webUser 令牌信息dto
     */
    public static void setWebUser(UserDO webUser) {
        USER.set(webUser);
    }

    /**
     * 获取当前的web用户信息
     *
     * @return {@link UserDO}
     */
    public static UserDO getWebUser() {
        return USER.get();
    }

    /**
     * 删除所有threadLocal缓存
     */
    public static void removeAll() {
        TOKEN.remove();
        USER.remove();
    }
}
