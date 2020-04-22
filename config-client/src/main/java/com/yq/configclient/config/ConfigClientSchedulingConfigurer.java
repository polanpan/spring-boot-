package com.polan.configclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * <p> 定时任务线程池配置</p>
 * @author youq  2019/1/3 16:54
 */
@Configuration
public class ConfigClientSchedulingConfigurer implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

}
