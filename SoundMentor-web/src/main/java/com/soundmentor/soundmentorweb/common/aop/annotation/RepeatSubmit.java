package com.soundmentor.soundmentorweb.common.aop.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    int limitTime() default 1;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    String errorMessage() default "请勿重复提交";
}
