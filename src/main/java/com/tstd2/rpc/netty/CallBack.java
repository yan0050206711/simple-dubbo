package com.tstd2.rpc.netty;


import com.tstd2.rpc.netty.model.Response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Rpc消息回调
 */
public class CallBack {

    private Response response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            if (this.response != null) {
                return this.response.getResult();
            }

            //设定一下超时时间，rpc服务器太久没有相应的话，就默认返回空吧。
            finish.await( 3000, TimeUnit.MILLISECONDS);

            if (this.response != null) {
                return this.response.getResult();
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(Response reponse) {
        try {
            lock.lock();
            finish.signal();
            this.response = reponse;
        } finally {
            lock.unlock();
        }
    }
}