package com.soundmentor.soundmentorweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@MapperScan(basePackages = "com.soundmentor.soundmentorweb.mapper")
public class SoundMentorWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundMentorWebApplication.class, args);
    }

}
