package com.soundmentor.soundmentorbase.utils;

import cn.hutool.core.util.StrUtil;
import com.soundmentor.soundmentorbase.common.request.RequestKeyRead;
import com.soundmentor.soundmentorbase.common.request.RequestParamMd5;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.annotation.RequestDuplicationCondition;
import com.soundmentor.soundmentorbase.utils.annotation.RequestLimitBlockingCondition;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RequestUtil {
    private static final Map<Class<? extends RequestKeyRead>, RequestKeyRead> KEY_READ_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<? extends RequestParamMd5>, RequestParamMd5> PARAM_MD5_MAP = new ConcurrentHashMap<>();

    /**
     * 获取requestKey的读取器
     *
     * @param tClass T级
     * @return {@link RequestKeyRead}
     */
    public static RequestKeyRead getRequestKeyRead(Class<? extends RequestKeyRead> tClass) {
        //获取 key 的读取类。
        return KEY_READ_MAP.computeIfAbsent(tClass, (key) -> {
            try {
                return tClass.newInstance();
            } catch (Exception e) {
                String message = StrUtil.format("【请求限流】获取请求限流的key生成类失败，类名称：{}, 失败原因：{}", tClass.getName(), e.getMessage());
                log.warn(message);
                throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), message);
            }
        });
    }

    /**
     * 获取请求参数md5的生成器
     *
     * @param tClass T级
     * @return {@link RequestKeyRead}
     */
    public static RequestParamMd5 getRequestParamMd5(Class<? extends RequestParamMd5> tClass) {
        //获取 key 的读取类。
        return PARAM_MD5_MAP.computeIfAbsent(tClass, (key) -> {
            try {
                return tClass.newInstance();
            } catch (Exception e) {
                String message = StrUtil.format("【请求限流】获取请求限流的参数md5生成类失败，类名称：{}, 失败原因：{}", tClass.getName(), e.getMessage());
                log.warn(message);
                throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(), message);
            }
        });
    }

    public static final String DUPLICATION = "DUPLICATION#";

    /**
     * 构建请求的key
     *
     * @param duplication 重复
     * @return {@link String}
     */
    public static String buildRequestKey(JoinPoint jp, RequestDuplicationCondition duplication) {
        //获取key读取方法
        RequestKeyRead read = getRequestKeyRead(duplication.keyReadClass());
        return DUPLICATION + duplication.keyPrefix() + (Objects.nonNull(read) ? read.readKeyValue(jp, duplication.key()) : duplication.key());
    }

    /**
     * 构建请求 param md5
     *
     * @param duplication 重复
     * @param jp          太平绅士
     * @return {@link String}
     */
    public static String buildRequestParamMd5(RequestDuplicationCondition duplication, JoinPoint jp) {
        if (duplication.limitParamMd5()) {
            RequestParamMd5 paramMd5 = getRequestParamMd5(duplication.paramMd5());
            return paramMd5.paramMd5(jp);
        }
        return "";
    }


    public static final String LIMIT_BLOCKING = "LIMIT:BLOCKING#";

    /**
     * 构建请求的key
     *
     * @param duplication 重复
     * @return {@link String}
     */
    public static String buildRequestKey(JoinPoint jp, RequestLimitBlockingCondition duplication) {
        //获取key读取方法
        RequestKeyRead read = getRequestKeyRead(duplication.keyReadClass());
        return LIMIT_BLOCKING + duplication.keyPrefix() + (Objects.nonNull(read) ? read.readKeyValue(jp, duplication.key()) : duplication.key());
    }
}