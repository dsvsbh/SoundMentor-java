package com.soundmentor.soundmentorbase.utils.base;

import com.soundmentor.soundmentorbase.utils.annotation.RequestLimitBlockingCondition;

/**
 * 请求限流的缓存
 *
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public interface RequestLimitCache {
    /**
     * 检查是否重复提交。true，允许提交，false不允许提交
     *
     * @param key               钥匙
     * @param value             价值
     * @param requestIntervalMs 请求间隔 ms
     * @return {@link Boolean}
     */
    Boolean checkDuplicationRequest(String key, String value, Long requestIntervalMs);

    /**
     * 请求限流拉黑。true，允许提交，false不允许提交
     *
     * @param key       钥匙
     * @param condition 条件
     * @return {@link Boolean}
     */
    Boolean requestLimitBlocking(String key, RequestLimitBlockingCondition condition);

    /**
     * 获取锁钥匙
     *
     * @param key 钥匙
     * @return {@link String}
     */
    default String getLockKey(String key) {
        return key + LOCK;
    }

    String LOCK = "#LOCK";
}
