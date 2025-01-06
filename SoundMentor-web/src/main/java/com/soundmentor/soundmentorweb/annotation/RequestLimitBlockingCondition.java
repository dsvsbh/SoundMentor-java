package com.soundmentor.soundmentorweb.annotation;

import com.soundmentor.soundmentorbase.common.request.RequestKeyRead;
import com.soundmentor.soundmentorbase.enums.base.LimitCacheEnum;

import java.lang.annotation.*;

/**
 * 请求限流拉黑。在请求达到一定频率的时候会进行拉黑操作
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimitBlockingCondition {
    /**
     * key的前缀，用来组合key进行限制
     *
     * @return {@link String}
     */
    String keyPrefix() default "";

    /**
     * 钥匙。与 keyPrefix 拼接进行限制
     *
     * @return {@link String}
     */
    String key();

    /**
     * Key的读取类。支持自定义对key进行读取。如果不传时，会默认是 RequestKeyRead 。然后直接使用 key 属性
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link RequestKeyRead}>
     */
    Class<? extends RequestKeyRead> keyReadClass() default RequestKeyRead.class;

    /**
     * 缓存类型
     *
     * @return {@link LimitCacheEnum}
     */
    LimitCacheEnum cacheType() default LimitCacheEnum.REDIS;

    /**
     * 请求阈值的时间单位。单位s
     *
     * @return {@link Long}
     */
    long requestThresholdTime();

    /**
     * 请求阈值。 在请求时间间隔内，达到了请求次数。就会被封禁
     *
     * @return long
     */
    int requestThreshold();

    /**
     * 封禁时间,单位s 为0时永久封禁
     *
     * @return long
     */
    long prohibitionTime();
}