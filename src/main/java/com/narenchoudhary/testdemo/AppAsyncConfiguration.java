package com.narenchoudhary.testdemo;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Profile("!test")
public class AppAsyncConfiguration implements AsyncConfigurer{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AppAsyncConfiguration.class);
	
	@Override
	@Bean("appThreadPoolTaskExecutor")
	public Executor getAsyncExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setMaxPoolSize(100);
	    executor.setCorePoolSize(10);
	    executor.setQueueCapacity(500);
	    executor.setThreadNamePrefix("appThreadPoolTaskExecutor-");
	    executor.initialize();
	    return executor;
	}
	
	@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects)
                -> LOGGER.error("Uncaught async exception.", throwable);
    }
}