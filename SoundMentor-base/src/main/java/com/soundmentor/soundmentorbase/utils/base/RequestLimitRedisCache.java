package com.soundmentor.soundmentorbase.utils.base;

import cn.hutool.core.collection.CollUtil;
import com.soundmentor.soundmentorbase.utils.RedisCache;
import com.soundmentor.soundmentorbase.utils.annotation.RequestLimitBlockingCondition;


import com.soundmentor.soundmentorbase.utils.base.redisConfig.RedisAutoConfiguration;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 请求限制Redis缓存
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public class RequestLimitRedisCache implements RequestLimitCache {
    @Resource
    private RedisCache redisCache;

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
        //进行尝试性插入， 插入失败，则认为重复提交冷却没有到时间。
        return redisCache.setNx(key, value, requestIntervalMs, TimeUnit.MILLISECONDS);
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
        boolean lock = checkLock(lockKey);
        if (lock) {
            return false;
        }
        //获取当前秒级时间戳
        long currentTime = System.currentTimeMillis();
        //获取开始时间
        long startTime = currentTime - (condition.requestThresholdTime() * 1000L);

        //进行批量操作
        redisCache.syncExec((redisTemplate) -> redisTemplate.executePipelined((RedisCallback<Boolean>) connection -> {
            //更新发送记录插入每次插入都更新有效时间为1天 不需要进行数据删除，请求次数达到阈值后锁定时间远大于过期时间。发送记录会先行过期
            //如果是手动解锁，则需要对此记录进行同步清理删除
            connection.zSetCommands().zAdd(RedisAutoConfiguration.STRING_SERIALIZER.serialize(key), currentTime, RedisAutoConfiguration.SERIALIZER.serialize(currentTime));
            connection.zSetCommands().zRemRangeByScore(RedisAutoConfiguration.STRING_SERIALIZER.serialize(key), 0L, startTime);
            //刷新设置zSet过期时间 为请求阈值限制 + 1分钟。确保redis不会缓存太多的数据
            connection.expire(RedisAutoConfiguration.STRING_SERIALIZER.serialize(key), condition.requestThresholdTime() + 60L);
            return null;
        }));
        //获取调用次数截取段
        Set<Object> set = redisCache.getRedisTemplate().opsForZSet().rangeByLex(key,
                new RedisZSetCommands.Range().gte(startTime).lte(currentTime));
        //查询到的数据大于阈值。 说明需要进行锁定
        if (CollUtil.isNotEmpty(set) && set.size() >= condition.requestThreshold()) {
            if (condition.prohibitionTime() == 0) {
                redisCache.set(lockKey, lockKey);
            } else {
                redisCache.setEx(lockKey, lockKey, condition.prohibitionTime());
            }
            return false;
        }
        return true;
    }

    /**
     * 检查是否锁定
     *
     * @param lockKey 锁钥匙
     * @return {@link Boolean}
     */
    private Boolean checkLock(String lockKey) {
        long survivalTime = redisCache.getSurvivalTime(lockKey);
        return survivalTime == -1 || survivalTime > 0;
    }
}