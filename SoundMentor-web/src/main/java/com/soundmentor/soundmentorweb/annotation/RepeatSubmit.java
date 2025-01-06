package com.soundmentor.soundmentorweb.annotation;

import org.apache.commons.lang3.time.TimeZones;

import java.lang.annotation.*;
import java.sql.Time;
import java.util.TimeZone;
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
