package com.flowingbit.data.collect.house_spider.async;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author homva
 * @date 2020/5/6
 *
 */

public class ThreadPoolExecutorBuilder {

    private ThreadPoolExecutorBuilder() {
    }

    /**
     * 创建未初始化的ThreadPoolTaskExecutor，由调用方customize后自行初始化
     * 如果传入的数值参数不合法则抛出异常
     *
     * @param corePoolSize
     * @param maxPoolSize
     * @param keepAliveSeconds
     * @param queueCapacity
     * @param threadNamePrefix
     * @param rejectedExecutionHandler
     * @return
     */
    public static ThreadPoolTaskExecutor buildUninitedThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, int queueCapacity,
                                                                             String threadNamePrefix,
                                                                             RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        if (!StringUtils.isBlank(threadNamePrefix)) {
            threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        }
        if (rejectedExecutionHandler != null) {
            threadPoolTaskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
        }
        return threadPoolTaskExecutor;
    }

    /**
     * 创建初始化的ThreadPoolTaskExecutor，调用方不能再配置
     * 如果传入的数值参数不合法则抛出异常
     *
     * @param corePoolSize
     * @param maxPoolSize
     * @param keepAliveSeconds
     * @param queueCapacity
     * @param threadNamePrefix
     * @param rejectedExecutionHandler
     * @return
     */
    public static ThreadPoolTaskExecutor buildInitedThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, int queueCapacity,
                                                                           String threadNamePrefix,
                                                                           RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = buildUninitedThreadPoolTaskExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, queueCapacity, threadNamePrefix, rejectedExecutionHandler);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
