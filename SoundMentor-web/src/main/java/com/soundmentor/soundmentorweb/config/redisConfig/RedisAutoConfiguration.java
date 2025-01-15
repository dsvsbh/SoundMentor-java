package com.soundmentor.soundmentorweb.config.redisConfig;

import cn.hutool.core.util.StrUtil;
import com.soundmentor.soundmentorweb.biz.util.cache.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

/**
 * redis配置
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties
public class RedisAutoConfiguration {
    @Value("${spring.redis.host}")
    private String REDIS_HOST;
    @Value("${spring.redis.port}")
    private String REDIS_PORT;
    /**
     * 如果ssl为true，则注入ssl模式
     *
     * @return {@link LettuceClientConfigurationBuilderCustomizer}
     */
    @Bean
    @ConditionalOnProperty(value = {"spring.redis.ssl"}, havingValue = "true")
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return (clientConfigurationBuilder) -> {
            clientConfigurationBuilder.useSsl().disablePeerVerification();
        };
    }
    public static final RedisSerializer<String> STRING_SERIALIZER = new StringRedisSerializer();
    public static final FastJson2JsonRedisSerializer<Object> SERIALIZER = new FastJson2JsonRedisSerializer<Object>(Object.class);

    /**
     * redis序列化配置
     *
     * @param connectionFactory 连接工厂
     * @return {@link RedisTemplate}<{@link String}, {@link Object}>
     * @since 1.0.0
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);

        //解决json转换
        template.setValueSerializer(SERIALIZER);
        //解决哈唏值转换
        template.setHashValueSerializer(SERIALIZER);
        // 防止乱码
        template.setKeySerializer(STRING_SERIALIZER);
        template.setHashKeySerializer(STRING_SERIALIZER);
        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public RedisUtil getRedisCache() {
        return new RedisUtil();
    }

    @Bean
    public RedissonClient redissonClient() {
        // 如果有密码则填写
        Config config = new Config();
        config.useSingleServer()
                .setAddress(StrUtil.format("redis://{}:{}", REDIS_HOST, REDIS_PORT));
        return Redisson.create(config);
    }
}

