package com.tstd2.soa.remoting.exchange;


import com.tstd2.soa.remoting.ErrorCode;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Rpc消息回调
 */
public class ResponseFuture {

    private Request request;
    private Response response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public ResponseFuture(Request request) {
        this.request = request;
        ResponseHolder.put(request.getSessionId(), this);
    }

    public Object start(int timeout) throws Exception {
        try {
            lock.lock();
            if (this.response != null) {
                return this.response.getResult();
            }

            // 超时设置
            finish.await(timeout, TimeUnit.MILLISECONDS);

            if (this.response != null) {
                return this.response.getResult();
            } else {
                String msg = buildErrorMsg(timeout);
                throw new TimeoutException(msg);
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(Response response) {
        try {
            ResponseHolder.remove(request.getSessionId());
            lock.lock();
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }

    public void cancel() {
        Response errorResult = new Response();
        errorResult.setSessionId(request.getSessionId());
        errorResult.setErrorMsg("request future has been canceled.");
        errorResult.setResultCode(ErrorCode.ERROR.errorCode);
        response = errorResult;
        ResponseHolder.remove(request.getSessionId());
    }

    private String buildErrorMsg(int timeout) {
        StringBuilder builder = new StringBuilder();
        for (Class<?> type : request.getParametersType()) {
            builder.append(type.getName()).append(",");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        return String.format("Timeout by: %ss! Class: %s, method: %s(%s), sessionId: %s", timeout,
                request.getClassName(), request.getMethodName(), builder.toString(), request.getSessionId());
    }
}