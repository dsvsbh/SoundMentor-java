package com.soundmentor.soundmentorweb.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user")
@Data
public class UserProperties {
    /**
     * 用户最大声音数量
     **/
    private Integer maxSound;
}
