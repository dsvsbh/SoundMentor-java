package com.soundmentor.soundmentorweb.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云通义千问大模型配置类
 *
 * @author Claude
 * @since 2026-03-31
 */
@Component
@ConfigurationProperties(prefix = "dashscope")
@Data
public class DashScopeProperties {
    /**
     * API 密钥
     */
    private String apiKey;
}
