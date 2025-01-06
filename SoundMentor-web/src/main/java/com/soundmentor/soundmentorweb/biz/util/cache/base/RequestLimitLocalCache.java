package com.soundmentor.soundmentorweb.biz.util.cache.base;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.soundmentor.soundmentorweb.annotation.RequestLimitBlockingCondition;



import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 请求限制Guava缓存
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public class RequestLimitLocalCache implements RequestLimitCache {
    /**
     * 数据源缓存。可以在定期时间内删除数据源，下次获取时重新生成
     *
     * @date 2024/05/22
     * @see ConcurrentHashMap<String, Long>
     */
    private static ConcurrentHashMap<String, Long> CACHE_MAP = new ConcurrentHashMap<>(), LOCK_CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 限流拉黑的缓存。记录每次请求的时间段
     *
     * @date 2024/05/24
     * @see
     */
    public static ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> LIMIT_BLOCKING_CACHE = new ConcurrentHashMap<>();

    /**
     * 检查是否重复提交。true，允许提交，false不允许提交
     *
     * @param key               钥匙
     * @param value             价值
     * @param requestIntervalMs 请求间隔 ms
     * @return {@link Boolean}
     */
    @Override
    public Boolean checkDuplicationRequest(String key, String value, Long requestIntervalMs) {
        String keyValue = key + value;
        long currentMs = System.currentTimeMillis(), newExpirationTime = currentMs + requestIntervalMs;
        //判断map存储的时间是否为null，不为null时，判断时间是否小于当前时间，是则更新，否则则不更新。
        long expirationTime = CACHE_MAP.compute(keyValue, (k, v) -> Objects.isNull(v) || v <= currentMs ? newExpirationTime : v);
        //如果更新成功，则认为插入成功了。否则既视为插入失败。
        return newExpirationTime == expirationTime;
    }

    /**
     * 请求限流拉黑。true，允许提交，false不允许提交
     *
     * @param key       钥匙
     * @param condition 条件
     * @return {@link Boolean}
     */
    @Override
    public Boolean requestLimitBlocking(String key, RequestLimitBlockingCondition condition) {
        String lockKey = getLockKey(key);
        Long expirationTime = LOCK_CACHE_MAP.get(lockKey);
        long currentTime = System.currentTimeMillis();
        //如果过期时间存在，且大于当前时间，则视为已经拉黑
        if (Objects.nonNull(expirationTime) && expirationTime > currentTime) {
            return false;
        }
        ConcurrentLinkedQueue<Long> queue = LIMIT_BLOCKING_CACHE.compute(key, (k, v) -> {
            if (Objects.isNull(v)) {
                v = new ConcurrentLinkedQueue<>();
            }
            v.add(currentTime);
            return v;
        });
        //获取开始时间
        long startTime = currentTime - (condition.requestThresholdTime() * 1000L);
        //删除不需要的请求数据
        queue.removeIf((v) -> v < startTime);
        //队列长度大于限流的阈值长度
        if (queue.size() >= condition.requestThreshold()) {
            if (condition.prohibitionTime() == 0L) {
                //获取10年后的时间戳
                LOCK_CACHE_MAP.put(lockKey, DateUtil.date().offset(DateField.YEAR, 10).getTime());
            } else {
                LOCK_CACHE_MAP.put(lockKey, currentTime + (condition.prohibitionTime() * 1000L));
            }
            //队列清空
            queue.clear();
        }
        return !LOCK_CACHE_MAP.containsKey(lockKey);
    }
}