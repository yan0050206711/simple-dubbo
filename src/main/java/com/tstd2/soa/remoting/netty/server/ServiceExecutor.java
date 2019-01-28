package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.config.Protocol;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务方法执行器
 */
public class ServiceExecutor {

    private volatile static ExecutorService threadPool;

    private static final AtomicInteger mThreadNum = new AtomicInteger(0);

    public static void submit(Callable<Object> call, int threads, Protocol protocol) {

        if (threadPool == null) {
            synchronized (ServiceExecutor.class) {
                if (threadPool == null) {
                    threadPool = initThreadPool(threads, protocol);
                }
            }
        }

//        Future<Object> future = threadPool.submit(call);
//        try {
//            future.get(timeout, TimeUnit.MILLISECONDS);
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//            future.cancel(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            future.cancel(true);
//        }

        // 1.服务端future.get会阻塞线程
        // 2.future.cancel不会立刻停止当前线程正在执行的任务，而是给当前线程设置一个中断标识，使后续该线程中断而不是当前任务
        // 3.服务端不做任务超时终端处理，超时控制由消费端控制
        threadPool.submit(call);
    }

    private static ThreadPoolExecutor initThreadPool(int threads, Protocol protocol) {
        final String threadName = "SimpleDubboThread-" + protocol.getHost() + ":" + protocol.getPort();
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maxPoolSize = threads > corePoolSize ? threads : corePoolSize;
        return new ThreadPoolExecutor(maxPoolSize, maxPoolSize, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread ret = new Thread(runnable, threadName + "-" + mThreadNum.incrementAndGet());
                ret.setDaemon(true);
                return ret;
            }
        }, new AbortPolicyWithReport(threadName, protocol));
    }

    static class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

        String threadName;
        Protocol protocol;

        AbortPolicyWithReport(String threadName, Protocol protocol) {
            this.threadName = threadName;
            this.protocol = protocol;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Thread pool is EXHAUSTED!" +
                            " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                            " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s://%s:%s!",
                    threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                    e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating(),
                    protocol.getName(), protocol.getHost(), protocol.getPort());
            throw new RejectedExecutionException(msg);
        }
    }

}
