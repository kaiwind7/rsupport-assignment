package com.rsupport.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 최소 스레드 수
        executor.setMaxPoolSize(50);   // 최대 스레드 수
        executor.setQueueCapacity(100); // 큐의 크기
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
