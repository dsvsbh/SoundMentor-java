package com.soundmentor.soundmentorweb.config.jucConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ThreadPoolConfig {
    @Bean("task-thread-pool-executor")
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                50,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(300),
                (r, executor) -> {
                    log.info("线程池已满，任务{},被丢弃", r.toString());
                }
        );
        return threadPoolExecutor;
    }
}
