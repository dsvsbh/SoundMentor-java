package com.soundmentor.soundmentorweb.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorbase.utils.JwtUtil;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final RedisTemplate redisTemplate;
    private final UserInfoApi userInfoApi;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = request.getHeader("Authorization");
            AssertUtil.hasLength(token, "Authorization不能为空!");
            Map<String, Object> stringObjectMap = JwtUtil.validateToken(token);
            String userId = stringObjectMap.get("userId").toString();
            AssertUtil.notNull(userId, "token已失效，请重新登录！");
            UserDO userDO = (UserDO)redisTemplate.opsForValue().get(StrUtil.format(SoundMentorConstant.REDIS_USER_KEY, userId));
            AssertUtil.ifNull(userDO, "用户信息已失效，请重新登录！");
            userInfoApi.setUser(userDO);
            return true;
        } catch (Exception e) {
            log.info("用户未认证，请先登录");
            throw new BizException(ResultCodeEnum.UNAUTHORIZED.getCode(), ResultCodeEnum.UNAUTHORIZED.getMsg());
        }
    }
}
