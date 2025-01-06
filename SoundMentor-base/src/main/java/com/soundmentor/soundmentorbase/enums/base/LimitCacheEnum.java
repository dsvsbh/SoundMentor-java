package com.soundmentor.soundmentorbase.enums.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限制缓存枚举
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Getter
@AllArgsConstructor
public enum LimitCacheEnum {
    /**
     * 当地
     *
     * @date 2024/05/21
     * @see LimitCacheEnum
     */
    LOCAL("本地缓存"),

    /**
     * 瑞迪斯
     *
     * @date 2024/05/21
     * @see LimitCacheEnum
     */
    REDIS("redis缓存"),
    ;

    /**
     * 状态码
     *
     * @since 1.0.0
     */
    private final String cacheDesc;
}
