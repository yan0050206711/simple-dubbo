package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.common.ThreadPoolFactory;
import com.tstd2.soa.config.ProtocolBean;

import java.util.concurrent.*;

/**
 * 业务方法执行器
 */
public class ServiceExecutor {

    private volatile static ExecutorService threadPool;

    public static void submit(Callable<Object> call, int threads, ProtocolBean protocol) {

        if (threadPool == null) {
            synchronized (ServiceExecutor.class) {
                if (threadPool == null) {
                    threadPool = ThreadPoolFactory.initThreadPool(threads, protocol);
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

}
