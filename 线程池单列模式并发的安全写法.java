package com.haylion.maastaxi.service;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangxin
 * @类描述
 * @time 2020/4/1  15:11
 */
public class SinglePool {

    private ThreadPoolExecutor threadPoolExecutor;

    private SinglePool(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public static void doExecute(Runnable task) {

    }

    private static SinglePool getInstance() {
        return InstanceHolder.SINGLE_POOL;
    }

    private static class InstanceHolder {
        private static final Integer CORE_POOL_SIZE = 50;
        private static final Integer MAX_POOL_SIZE = 500;
        private static final Long KEEP_ALIVE_TIME = 2L;
        private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
        private static final LinkedTransferQueue QUEUE = new LinkedTransferQueue();

        private static final ThreadFactory FACTORY = new ThreadFactory() {
            private final AtomicInteger integer = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "SysThreadPool thread" + integer.getAndIncrement());
            }
        };

        public static final SinglePool SINGLE_POOL = new SinglePool(
                new ThreadPoolExecutor(
                        CORE_POOL_SIZE,
                        MAX_POOL_SIZE,
                        KEEP_ALIVE_TIME,
                        TIME_UNIT,
                        QUEUE,
                        FACTORY
                ));
    }

}
