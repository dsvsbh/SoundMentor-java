package com.soundmentor.soundmentorweb.aspects;

import com.soundmentor.soundmentorbase.enums.base.LimitCacheEnum;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorweb.annotation.RequestDuplicationCondition;
import com.soundmentor.soundmentorweb.biz.util.cache.base.RequestLimitLocalCache;
import com.soundmentor.soundmentorweb.biz.util.cache.base.RequestLimitRedisCache;
import com.soundmentor.soundmentorweb.biz.util.limit.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * 请求重复切面
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Slf4j
@Order
@Aspect
public class RequestDuplicationAspect {
    @Resource
    private RequestLimitLocalCache requestLimitLocalCache;
    @Resource
    private RequestLimitRedisCache requestLimitRedisCache;
    @Pointcut(value = "@annotation(com.soundmentor.soundmentorweb.annotation.RequestDuplicationCondition)")
    public void action() {
    }

    /**
     * 前置切面
     *
     * @param jp          切入点
     * @param duplication 重复
     */
    @Before(value = "@annotation(duplication)")
    public void before(JoinPoint jp, RequestDuplicationCondition duplication) {
        //切面错误的切入方法
        AssertUtil.notNull(duplication, "系统异常，请稍后重试~");
        //获取缓存的key
        String key = RequestUtil.buildRequestKey(jp, duplication);
        //获取参数的md5
        String paramMd5 = RequestUtil.buildRequestParamMd5(duplication, jp);
        if (duplication.cacheType().equals(LimitCacheEnum.LOCAL)) {
            AssertUtil.isTrue(requestLimitLocalCache.checkDuplicationRequest(key, paramMd5, duplication.requestIntervalMs()),
                    duplication.errorCode(), "请求频繁，请稍后重试");
        } else if (duplication.cacheType().equals(LimitCacheEnum.REDIS)) {
            AssertUtil.isTrue(requestLimitRedisCache.checkDuplicationRequest(key, paramMd5, duplication.requestIntervalMs()),
                    duplication.errorCode(), "请求频繁，请稍后重试");
        }
    }
}
