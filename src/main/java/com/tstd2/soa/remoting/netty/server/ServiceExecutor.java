package com.tstd2.soa.remoting.netty.server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务方法执行器
 */
public class ServiceExecutor {

    private volatile static ExecutorService threadPool;

    private static final AtomicInteger mThreadNum = new AtomicInteger(1);

    public static void submit(Callable<Object> call, int threads, int timeout) {

        if (threadPool == null) {
            synchronized (ServiceExecutor.class) {
                if (threadPool == null) {
                    threadPool = initThreadPool(threads);
                }
            }
        }

        Future<Object> future = threadPool.submit(call);
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            future.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
            future.cancel(true);
        }
    }

    private static ThreadPoolExecutor initThreadPool(int threads) {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maxPoolSize = threads > corePoolSize ? threads : corePoolSize;
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread ret = new Thread(runnable, "SimpleDubbo-thread-" + mThreadNum.incrementAndGet());
                ret.setDaemon(true);
                return ret;
            }
        });
    }

}
