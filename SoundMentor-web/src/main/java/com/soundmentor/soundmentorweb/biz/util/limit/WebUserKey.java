package com.soundmentor.soundmentorweb.biz.util.limit;

import cn.hutool.core.util.ArrayUtil;
import com.soundmentor.soundmentorbase.common.request.RequestKeyRead;
import com.soundmentor.soundmentorbase.utils.ElUtil;
import com.soundmentor.soundmentorweb.biz.util.treadLocal.UserAuthCenter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

/**
 * webUser的key
 *
 * @author 卫庄
 * @module
 * @date 2024/07/16
 **/
public class WebUserKey extends RequestKeyRead {
    /**
     * 读取key的实际值
     *
     * @param jp  切点的属性
     * @param key key
     * @return {@link String}
     */
    @Override
    public String readKeyValue(JoinPoint jp, String key) {
        if (ElUtil.containElString(key)) {
            Map<String, Object> map = this.readJoinPointArgs(jp);
            map.put("webUser", UserAuthCenter.getWebUser());
            return ElUtil.replaceElString(key, map);
        } else {
            return key;
        }
    }

    /**
     * 读取切点的param map
     *
     * @param joinPoint 加入点
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    protected Map<String, Object> readJoinPointArgs(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("Only method execution signatures are supported");
        }
        String[] paramNames = ((MethodSignature) signature).getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> map = new HashMap<>(paramNames.length);
        for (int i = 0; i < paramNames.length; i++) {
            map.put(paramNames[i], ArrayUtil.get(args, i));
        }
        return map;
    }
}