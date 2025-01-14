package com.soundmentor.soundmentorbase.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

/**
 * 时间转换工具类
 *
 * @Author: Make
 * @DATE: 2025/01/14
 **/
public final class LocalDateTimeUtils {
    /**
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     */
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期时间格式yyyy-MM-dd
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 构造函数
     */
    private LocalDateTimeUtils() {
        super();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date对象
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * yyyy-MM-dd格式的时间字符串转localDate
     */
    public static LocalDate dateStrToLocalDate(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return LocalDate.parse(str);
    }

    /**
     * localDate转换为yyyy-mm-hh格式字符串
     */
    public static String localDateToStr(LocalDate date) {
        if (date == null) {
            return null;
        }
        return formatDate(date, DATE_PATTERN);
    }


    /**
     * LocalDateTime 转时间戳 毫秒数
     *
     * @param dateTime Date对象
     * @return
     */
    public static Long localDateTimeToMillisecond(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * LocalDateTime 转时间戳 秒数
     *
     * @param dateTime Date对象
     * @return
     */
    public static Long localDateTimeToSecond(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 毫秒级时间戳转换成 LocalDateTime
     *
     * @param millisecond 时间戳
     */
    public static LocalDateTime millisecondToLocalDateTime(Long millisecond) {
        if (millisecond == null) {
            return null;
        }
        return new Date(millisecond)
                .toInstant()
                .atOffset(ZoneId.systemDefault().getRules().getOffset(Instant.now()))
                .toLocalDateTime();
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DATE_TIME_PATTERN);
    }

    /**
     * 按pattern格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @param pattern  要格式化的字符串
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_PATTERN;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 按pattern格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @param pattern  要格式化的字符串
     * @return
     */
    public static String formatDate(LocalDate dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_TIME_PATTERN;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 获取今天的00:00:00
     *
     * @return
     */
    public static String getDayStart() {
        return getDayStart(LocalDateTime.now());
    }

    /**
     * 获取今天的23:59:59
     *
     * @return
     */
    public static String getDayEnd() {
        return getDayEnd(LocalDateTime.now());
    }

    /**
     * 获取某天的00:00:00
     *
     * @param dateTime
     * @return
     */
    public static String getDayStart(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MIN));
    }

    /**
     * 获取某天的23:59:59
     *
     * @param dateTime
     * @return
     */
    public static String getDayEnd(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MAX));
    }

    /**
     * 获取本月第一天的00:00:00
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        return getFirstDayOfMonth(LocalDateTime.now());
    }

    /**
     * 获取本月最后一天的23:59:59
     *
     * @return
     */
    public static String getLastDayOfMonth() {
        return getLastDayOfMonth(LocalDateTime.now());
    }

    /**
     * 获取某月第一天的00:00:00
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String getFirstDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
    }

    /**
     * 获取某月最后一天的23:59:59
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String getLastDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));
    }
}