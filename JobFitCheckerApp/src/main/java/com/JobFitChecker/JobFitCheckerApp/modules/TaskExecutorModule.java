package com.JobFitChecker.JobFitCheckerApp.modules;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorModule {
    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);  // single-threaded for continuous polling
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(0); // no queue needed for single-threaded execution
        executor.setThreadNamePrefix("ResumePostProcessingExecutor-");
        executor.initialize();
        return executor;
    }
}
