package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Minimum iş parçacığı sayısı
        executor.setMaxPoolSize(20); // Maksimum iş parçacığı sayısı
        executor.setQueueCapacity(100); // Kuyruktaki maksimum iş sayısı
        executor.setThreadNamePrefix("ShippingTask-");
        executor.initialize();
        return executor;
    }
}
