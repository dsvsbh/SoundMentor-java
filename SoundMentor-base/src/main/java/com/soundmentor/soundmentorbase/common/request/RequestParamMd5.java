package com.soundmentor.soundmentorbase.common.request;

import cn.hutool.crypto.SecureUtil;
import org.aspectj.lang.JoinPoint;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Objects;

/**
 * 将请求参数进行MD5摘要
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public class RequestParamMd5 {
    public RequestParamMd5() {
    }
    public String paramMd5(JoinPoint jp) {
        Object[] requestParam = jp.getArgs();
        return Objects.nonNull(requestParam) ? SecureUtil.md5(JSONObject.toJSONString(requestParam, new SerializerFeature[]{SerializerFeature.SortField})) : SecureUtil.md5("");
    }
}