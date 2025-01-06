package com.soundmentor.soundmentorbase.common.request;

import com.soundmentor.soundmentorbase.utils.ElUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.core.util.ArrayUtil;

/**
 * 请求参数解析
 * @Author: Make
 * @DATE: 2025/01/06
 **/
public class RequestKeyRead {
    public RequestKeyRead() {
    }

    public String readKeyValue(JoinPoint jp, String key) {
        return ElUtil.containElString(key) ? ElUtil.replaceElString(key, this.readJoinPointArgs(jp)) : key;
    }

    protected Map<String, Object> readJoinPointArgs(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("Only method execution signatures are supported");
        } else {
            MethodSignature methodSignature = (MethodSignature)signature;
            Method method = methodSignature.getMethod();
            Parameter[] parameters = method.getParameters();
            Object[] args = joinPoint.getArgs();
            Map<String, Object> map = new HashMap(parameters.length);

            for(int i = 0; i < parameters.length; ++i) {
                map.put(parameters[i].getName(), ArrayUtil.get(args, i));
            }

            return map;
        }
    }
}
