package com.soundmentor.soundmentorweb.biz.util.cache.base;

import java.util.List;
import java.util.Map;

/**
 * 缓存操作定义接口
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public interface Cache {
    /**
     * 使用key获取对象.
     * 默认实现中(ai.ii.uic.user.cache.impl.LettuceCache), 将结果视作JSON字符串, 并反射到Java对象. 这要求保存对象时, 将@type写入对象的JSON字符串中.
     *
     * @param key 缓存的key
     * @param <T> 对象类型
     * @return Java类型的缓存对象
     */
    <T> T get(String key);

    /**
     * 使用key获取对象. 且将缓存数据作为字符串返回
     *
     * @param key 缓存的key
     * @return 字符串标示的缓存数据
     */
    String getStr(String key);

    /**
     * 保存缓存数据
     * 默认实现中(ai.ii.uic.user.cache.impl.LettuceCache), 先判断val是否基础类型:
     * 若是, 使用String.valueOf()将val转换为字符串
     * 若否, 使用fastJson将val转换成JSON字符串, 并将对象类名写入@type字段
     *
     * @param key 缓存的key
     * @param val 被缓存的数据
     * @return 是否保存成功
     */
    Boolean set(String key, Object val);

    /**
     * 保存缓存数据,并设置过期时间
     * 先判断val是否基础类型:
     * 若是, 使用String.valueOf()将val转换为字符串
     * 若否, 使用fastJson将val转换成JSON字符串, 并将对象类名写入@type字段
     *
     * @param key    缓存的key
     * @param val    被缓存的数据
     * @param expire 过期时间 单位 s
     * @return 是否保存成功
     */
    Boolean setEx(String key, Object val, Long expire);

    /**
     * 批量删除keys
     *
     * @param keys 缓存中key集合
     * @return 执行成功数量
     */
    int del(String... keys);

    /**
     * hdel
     *
     * @param k  k
     * @param fs fs
     * @return int
     */
    int hDel(String k, String... fs);

    /**
     * hash删除指定的field
     *
     * @param map key为redisKey value为hash字段
     * @return int
     */
    Boolean hDel(Map<String, List<String>> map);

    /**
     * redis hash 设置
     *
     * @param key   redis key
     * @param value 价值
     * @param field 字段
     * @return {@link Boolean}
     */
    Boolean hashSet(String key, String field, Object value);

    /**
     * redis hash 设置
     *
     * @param key         redis key
     * @param fieldValues 字段值的键值对，用于批量插入
     * @return {@link Boolean}
     */
    Boolean hashSet(String key, Map<String, Object> fieldValues);

    /**
     * redis hash 设置
     *
     * @param key         redis key
     * @param fieldValues 字段值的键值对，用于批量插入
     * @return {@link Boolean}
     */
    Boolean hashSetEx(String key, Map<String, Object> fieldValues, Long expire);

    /**
     * redis hash 获取 不存在时获取为null,类型不对时抛出json转换异常
     *
     * @param key   redis key
     * @param field 字段
     * @return {@link Boolean}
     */
    <T> T hashGet(String key, String field);

    /**
     * redis hash 获取所有hash的值
     *
     * @param key redis key
     * @return {@link Boolean}
     */
    List<String> hashValues(String key);

    /**
     * redis hash 获取所有的hash映射关系
     *
     * @param key redis key
     * @return {@link Map<String, String>}
     */
    Map<String, String> hashAll(String key);

    /**
     * 获取制定缓存的剩余有效时间
     *
     * @param key 缓存的key
     * @return 以毫秒为单位的整数值TTL或负值
     * -1, 永久存活，不到期。
     * -2, 如果键不存在。
     * >1 剩余的存活时间
     */
    Long getSurvivalTime(String key);

    /**
     * 更新key的过期时间
     *
     * @param key    缓存的key
     * @param expire 新的过期时间
     * @return
     */
    Boolean updateExpireTime(String key, Long expire);
}
