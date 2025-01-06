package com.soundmentor.soundmentorweb.biz.util.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorweb.biz.util.cache.base.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Slf4j
public class RedisUtil implements Cache {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 使用key获取对象.
     * 默认实现中(ai.ii.uic.user.cache.impl.LettuceCache), 将结果视作JSON字符串, 并反射到Java对象. 这要求保存对象时, 将@type写入对象的JSON字符串中.
     *
     * @param key 缓存的key
     * @param <T> 对象类型
     * @return Java类型的缓存对象
     */
    @Override
    public <T> T get(String key) {
        return syncExec(syncCmd -> redisResultTrans(syncCmd.opsForValue().get(key)));
    }

    /**
     * 批量获取
     *
     * @param keys 钥匙
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> multiGet(Collection<String> keys) {
        return syncExec(syncCmd -> Optional.ofNullable(syncCmd.opsForValue().multiGet(keys))
                .orElse(Collections.emptyList()).stream()
                .map(RedisUtil::<T>redisResultTrans)
                .collect(Collectors.toList()));
    }

    /**
     * 批量setNx
     *
     * @param map 地图
     * @return {@link Boolean}
     */
    public Boolean multiSetNx(Map<String, ?> map) {
        return syncExec(syncCmd -> syncCmd.opsForValue().multiSetIfAbsent(map));
    }

    /**
     * 使用key获取对象. 且将缓存数据作为字符串返回
     *
     * @param key 缓存的key
     * @return 字符串标示的缓存数据
     */
    @Override
    public String getStr(String key) {
        return syncExec(syncCmd -> redisResultTrans(syncCmd.opsForValue().get(key)));
    }

    @Override
    public Boolean set(String key, Object val) {
        return syncExec(syncCmd -> {
            syncCmd.opsForValue().set(key, valueTransformation(val));
            return true;
        });
    }

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
    @Override
    public Boolean setEx(String key, Object val, Long expire) {
        return syncExec(syncCmd -> {
            syncCmd.opsForValue().set(key, valueTransformation(val), expire, TimeUnit.SECONDS);
            return true;
        });
    }

    /**
     * redis设置key value。key不存在时插入
     *
     * @param key    关键
     * @param val    瓦尔
     * @param expire 到期时间，单位s
     * @return {@link Boolean}
     */
    public Boolean setNx(String key, Object val, Long expire) {
        return setNx(key, val, expire, TimeUnit.SECONDS);
    }

    /**
     * redis设置key value。key不存在时插入
     *
     * @param key      关键
     * @param val      瓦尔
     * @param expire   到期
     * @param timeUnit 时间单位
     * @return {@link Boolean}
     */
    public Boolean setNx(String key, Object val, Long expire, TimeUnit timeUnit) {
        return syncExec(syncCmd -> syncCmd.boundValueOps(key).setIfAbsent(valueTransformation(val), expire, timeUnit));
    }

    /**
     * 批量删除keys
     *
     * @param keys 缓存中key集合
     * @return 执行成功数量
     */
    @Override
    public int del(String... keys) {
        if (ArrayUtil.isAllEmpty(keys)) {
            return 0;
        }
        return Optional.ofNullable(syncExec(syncCmd -> syncCmd.delete(Arrays.asList(keys))))
                .map(Long::intValue)
                .orElse(0);
    }

    /**
     * hash删除指定的field
     *
     * @param k  k
     * @param fs fs
     * @return int
     */
    @Override
    public int hDel(String k, String... fs) {
        if (ArrayUtil.isAllEmpty(fs)) {
            return 0;
        }
        return Optional.ofNullable(syncExec(syncCmd -> syncCmd.opsForHash().delete(k, fs)))
                .map(Long::intValue)
                .orElse(0);
    }

    /**
     * hash删除指定的field
     *
     * @param map key为redisKey value为hash字段
     * @return int
     */
    @Override
    public Boolean hDel(Map<String, List<String>> map) {
        if (MapUtil.isEmpty(map)) {
            return true;
        }
        return Optional.ofNullable(syncExec(syncCmd -> {
            for (Map.Entry<String, List<String>> it : map.entrySet()) {
                List<Object> list = new ArrayList<>(it.getValue());
                syncCmd.opsForHash().delete(it.getKey(), ArrayUtil.toArray(list, Object.class));
            }
            return true;
        })).orElse(false);
    }

    /**
     * redis hash 设置
     *
     * @param key   redis key
     * @param value 价值
     * @param field 字段
     * @return {@link Boolean}
     */
    @Override
    public Boolean hashSet(String key, String field, Object value) {
        return syncExec(syncCmd -> {
            syncCmd.opsForHash().put(key, field, valueTransformation(value));
            return true;
        });
    }

    /**
     * 获取所有redisHash的key
     *
     * @param key 关键
     * @return {@link List}<{@link String}>
     */
    public List<String> hashKeys(String key) {
        return syncExec(syncCmd -> {
            Set<Object> set = syncCmd.opsForHash().keys(key);
            return CollUtil.isNotEmpty(set) ? set.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()) : Collections.emptyList();
        });
    }

    /**
     * redis设置hash的nx。key、value
     *
     * @param key   关键
     * @param field 场
     * @param value 价值
     * @return {@link Boolean}
     */
    public Boolean hashSetNx(String key, String field, Object value) {
        return syncExec(syncCmd -> {
            syncCmd.boundHashOps(key).putIfAbsent(field, valueTransformation(value));
            return true;
        });
    }

    /**
     * redis hash 设置
     *
     * @param key         redis key
     * @param fieldValues 字段值的键值对，用于批量插入
     * @return {@link Boolean}
     */
    @Override
    public Boolean hashSet(String key, Map<String, Object> fieldValues) {
        if (MapUtil.isEmpty(fieldValues)) {
            return true;
        }
        Map<String, Object> map = new HashMap<>(fieldValues.size());
        for (Map.Entry<String, Object> it : fieldValues.entrySet()) {
            map.put(it.getKey(), valueTransformation(it.getValue()));
        }
        return syncExec(syncCmd -> {
            syncCmd.opsForHash().putAll(key, map);
            return true;
        });
    }

    /**
     * redis hash 设置
     *
     * @param key         redis key
     * @param fieldValues 字段值的键值对，用于批量插入
     * @return {@link Boolean}
     */
    @Override
    public Boolean hashSetEx(String key, Map<String, Object> fieldValues, Long expire) {
        if (MapUtil.isEmpty(fieldValues)) {
            return true;
        }
        Map<String, Object> map = new HashMap<>(fieldValues.size());
        for (Map.Entry<String, Object> it : fieldValues.entrySet()) {
            map.put(it.getKey(), valueTransformation(it.getValue()));
        }
        return syncExec(syncCmd -> {
            syncCmd.opsForHash().putAll(key, map);
            return syncCmd.expire(key, expire, TimeUnit.SECONDS);
        });
    }

    /**
     * redis hash 获取 不存在时获取为null,类型不对时抛出json转换异常
     *
     * @param key   redis key
     * @param field 字段
     * @return {@link Boolean}
     */
    @Override
    public <T> T hashGet(String key, String field) {
        return syncExec(syncCmd -> redisResultTrans(syncCmd.opsForHash().get(key, field)));
    }

    public static Object valueTransformation(Object value) {
        if (value.getClass().isPrimitive()) {
            return String.valueOf(value);
        } else {
            return value;
        }
    }

    /**
     * redis结果转换
     *
     * @param redisResult 复述,结果
     * @return {@link T}
     */
    public static <T> T redisResultTrans(Object redisResult) {
        if (Objects.isNull(redisResult)) {
            return null;
        }
        if (redisResult.getClass().isPrimitive()) {
            return (T) String.valueOf(redisResult);
        } else {
            return (T) redisResult;
        }
    }

    /**
     * redis hash 获取所有hash的值
     *
     * @param key redis key
     * @return {@link Boolean}
     */
    @Override
    public List<String> hashValues(String key) {
        return syncExec(syncCmd -> syncCmd.opsForHash().values(key).stream().map(Object::toString).collect(Collectors.toList()));
    }

    /**
     * 获取所有的hash value
     *
     * @param key 关键
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> hashAllValue(String key) {
        List<Object> value = syncExec(syncCmd -> syncCmd.opsForHash().values(key));
        return CollUtil.isNotEmpty(value) ? value.stream()
                .map(RedisUtil::<T>redisResultTrans)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    /**
     * redis hash 获取所有的hash映射关系
     *
     * @param key redis key
     * @return {@link Map<String, String>}
     */
    @Override
    public Map<String, String> hashAll(String key) {
        return syncExec(syncCmd -> {
            Map<String, String> resultMap = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> it : syncCmd.opsForHash().entries(key).entrySet()) {
                resultMap.put(it.getKey().toString(), it.getValue().toString());
            }
            return resultMap;
        });
    }

    /**
     * redis hash 获取所有的hash映射关系
     *
     * @param key 关键
     * @return {@link Map}<{@link String}, {@link T}>
     */
    public <T> Map<String, T> hashAllToMap(String key) {
        return syncExec(syncCmd -> {
            Map<String, T> resultMap = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> it : syncCmd.opsForHash().entries(key).entrySet()) {
                resultMap.put(it.getKey().toString(), redisResultTrans(it.getValue()));
            }
            return resultMap;
        });
    }

    /**
     * 获取制定缓存的剩余有效时间
     *
     * @param key 缓存的key
     * @return 以毫秒为单位的整数值TTL或负值
     * -1, 永久存活，不到期。
     * -2, 如果键不存在。
     * >1 剩余的存活时间
     */
    @Override
    public Long getSurvivalTime(String key) {
        return syncExec(syncCmd -> syncCmd.getExpire(key));
    }

    /**
     * 更新key的过期时间
     *
     * @param key    缓存的key
     * @param expire 新的过期时间
     * @return
     */
    @Override
    public Boolean updateExpireTime(String key, Long expire) {
        return syncExec(syncCmd -> syncCmd.expire(key, expire, TimeUnit.SECONDS));
    }

    /**
     * zSet批量新增
     *
     * @return {@link Boolean}
     */
    public Boolean zSetAdd(String redisKey, Set<ZSetOperations.TypedTuple<Object>> valueSet) {
        return syncExec(syncCmd -> {
            Long result = syncCmd.boundZSetOps(redisKey).add(valueSet);
            return Objects.nonNull(result) && result.intValue() == valueSet.size();
        });
    }

    /**
     * zSet新增
     *
     * @param redisKey 复述,关键
     * @param value    价值
     * @param score    分数
     * @return {@link Boolean}
     */
    public Boolean zSetAdd(String redisKey, Object value, double score) {
        return syncExec(syncCmd -> syncCmd.boundZSetOps(redisKey).add(value, score));
    }

    /**
     * zSet按照分数范围查找
     *
     * @param redisKey 复述,关键
     * @param min      最小值
     * @param max      马克斯
     * @return {@link Set}<{@link T}>
     */
    public <T> Set<T> zSetRangeByScore(String redisKey, double min, double max) {
        Set<Object> set = syncExec(syncCmd -> syncCmd.boundZSetOps(redisKey).rangeByScore(min, max));
        return CollUtil.isNotEmpty(set) ?
                set.stream().map(RedisUtil::<T>redisResultTrans).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    /**
     * zSet按照分数范围删除
     *
     * @param redisKey 复述,关键
     * @param min      最小值
     * @param max      马克斯
     * @return {@link Boolean}
     */
    public Boolean zSetRemoveRangeByScore(String redisKey, double min, double max) {
        return syncExec(syncCmd -> {
            syncCmd.boundZSetOps(redisKey).removeRangeByScore(min, max);
            return true;
        });
    }

    public <R> R syncExec(Function<RedisTemplate<String, Object>, R> function) {
        try {
            return function.apply(redisTemplate);
        } catch (Exception e) {
            log.error("Redis连接获取失败: ", e);
            throw new BizException("Redis连接获取失败");
        }
    }
}
