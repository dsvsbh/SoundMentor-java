package com.soundmentor.soundmentorweb.common.aop.aspects;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSON;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AESUtil;
import com.soundmentor.soundmentorweb.common.aop.annotation.RepeatSubmit;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class RepeatSubmitAspect {
    private final UserInfoApi userInfoApi;
    private final RedisTemplate redisTemplate;
    @Pointcut("@annotation(com.soundmentor.soundmentorweb.common.aop.annotation.RepeatSubmit)")
    public void repeatSubmit() {
    }

    /**
     * 防止重复提交，在时间限制内相同用户请求相同方法并提交相同参数，则拒绝，白名单接口无法使用
     * @param joinPoint
     */
    @Before("repeatSubmit()")
    public void doBefore(JoinPoint joinPoint) {
        RepeatSubmit annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RepeatSubmit.class);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = JSON.toJSONString(args);
        Integer id = userInfoApi.getUser().getId();
        String key = SoundMentorConstant.REPEAT_SUBMIT_KEY + AESUtil.encrypt(id + className + methodName + argsString, SoundMentorConstant.AES_KEY);
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, "repeatSubmit", annotation.limitTime(), annotation.timeUnit());
        if(BooleanUtil.isFalse(b))
        {
            throw new BizException(ResultCodeEnum.REPEATED_OPERATION.getCode(), annotation.errorMessage());
        }
    }
}
