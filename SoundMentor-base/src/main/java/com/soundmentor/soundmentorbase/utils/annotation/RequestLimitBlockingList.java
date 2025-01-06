package com.soundmentor.soundmentorbase.utils.annotation;

import java.lang.annotation.*;

/**
 * 拉黑黑名单
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimitBlockingList {
    /**
     * 限流黑名单条件
     *
     * @return {@link List}<{@link RequestLimitBlockingCondition}>
     */
    RequestLimitBlockingCondition[] blockingList();
}