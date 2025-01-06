package com.soundmentor.soundmentorweb.config;

import com.soundmentor.soundmentorweb.controller.UserController;
import com.soundmentor.soundmentorweb.interceptor.AuthorizationInterceptor;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 配置拦截器等
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {
    private final RedisTemplate redisTemplate;
    private final UserInfoApi userInfoApi;

    /**
     * 替换 SpringBoot 默认Json 序列化/反序列化 工具为 fastJson
     *
     * @param converters 转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * 跨域配置
     *
     * @param registry 注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    /**
     * 注册拦截器，配置白名单
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(redisTemplate, userInfoApi))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/openApi/**"
                );
    }
}
