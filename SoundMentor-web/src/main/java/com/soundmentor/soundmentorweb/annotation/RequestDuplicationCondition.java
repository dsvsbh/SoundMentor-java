package com.soundmentor.soundmentorweb.annotation;

import com.soundmentor.soundmentorbase.common.request.RequestKeyRead;
import com.soundmentor.soundmentorbase.common.request.RequestParamMd5;
import com.soundmentor.soundmentorbase.enums.base.LimitCacheEnum;

import java.lang.annotation.*;


/**
 * 用于标记一个方法重复提交的注解。可以针对方法进行防止重复提交
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestDuplicationCondition {
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
     * 请求间隔毫秒
     *
     * @return {@link Long}
     */
    long requestIntervalMs();

    /**
     * 是否按照参数的md5进行限制
     *
     * @return boolean
     */
    boolean limitParamMd5() default false;

    /**
     * 参数md5读取。如果不为 RequestParamMd5 时，则会进行读取操作。并判断是否是同样参数。起到的效果则为一定时间内，不允许相同内容的重复提交
     * 如果是默认的 RequestParamMd5 时，则不会判断，起到的效果则为一定时间内不允许多次调用此接口
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link RequestParamMd5}>
     */
    Class<? extends RequestParamMd5> paramMd5() default RequestParamMd5.class;


    /**
     * 错误code
     * <p>
     * 默认是 ResultCodeEnum.INTERNAL_ERROR
     *
     * @return {@link String}
     */
    String errorCode() default "2025001";
}

