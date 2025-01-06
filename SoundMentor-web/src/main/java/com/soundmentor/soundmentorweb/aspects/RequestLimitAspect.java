package com.soundmentor.soundmentorweb.aspects;

import com.soundmentor.soundmentorbase.enums.base.LimitCacheEnum;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorweb.annotation.RequestLimitBlockingCondition;
import com.soundmentor.soundmentorweb.annotation.RequestLimitBlockingList;
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
 * 请求限流的的切面
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Slf4j
@Order
@Aspect
public class RequestLimitAspect {
    @Resource
    private RequestLimitLocalCache requestLimitLocalCache;
    @Resource
    private RequestLimitRedisCache requestLimitRedisCache;

    /**
     * 限流拉黑的切点
     */
    @Pointcut(value = "@annotation(com.soundmentor.soundmentorweb.annotation.RequestLimitBlockingCondition)")
    public void requestLimitBlockingAction() {
    }

    /**
     * 限流拉黑的切面
     *
     * @param jp        切入点
     * @param condition 条件
     */
    @Before(value = "@annotation(condition)")
    public void before(JoinPoint jp, RequestLimitBlockingCondition condition) {
        requestLimitBlocking(jp, condition);
    }

    /**
     * 请求限制阻止
     *
     * @param jp        jp
     * @param condition 条件
     */
    public void requestLimitBlocking(JoinPoint jp, RequestLimitBlockingCondition condition) {
        //切面错误的切入方法
        AssertUtil.notNull(condition, "系统异常，请稍后重试~");
        //获取缓存的key
        String key = RequestUtil.buildRequestKey(jp, condition);
        if (condition.cacheType().equals(LimitCacheEnum.LOCAL)) {
            AssertUtil.isTrue(requestLimitLocalCache.requestLimitBlocking(key, condition),
                    "请求频繁，请稍后重试");
        } else if (condition.cacheType().equals(LimitCacheEnum.REDIS)) {
            AssertUtil.isTrue(requestLimitRedisCache.requestLimitBlocking(key, condition),
                    "请求频繁，请稍后重试");
        }
    }

    /**
     * 限流拉黑的切点
     */
    @Pointcut(value = "@annotation(com.soundmentor.soundmentorweb.annotation.RequestLimitBlockingList)")
    public void requestLimitBlockingListAction() {
    }

    /**
     * 限流拉黑多个规则组合的切面
     *
     * @param jp           jp
     * @param blockingList 阻止列表
     */
    @Before(value = "@annotation(blockingList)")
    public void before(JoinPoint jp, RequestLimitBlockingList blockingList) {
        for (RequestLimitBlockingCondition blockingCondition : blockingList.blockingList()) {
            requestLimitBlocking(jp, blockingCondition);
        }
    }
}
