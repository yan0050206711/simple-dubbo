package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.remoting.ErrorCode;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Rpc消息回调
 */
public class DefaultFuture extends CompletableFuture implements ResponseFuture {

    private final int DEFAULT_TIMEOUT = 3000;
    private Request request;
    private volatile Response response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();
    private volatile boolean cancelled;

    public DefaultFuture(Request request) {
        this.request = request;
        ResponseHolder.put(request.getSessionId(), this);
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            return this.get(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            lock.lock();
            if (this.isDone()) {
                return this.returnFromResponse();
            }

            // 超时设置
            finish.await(timeout, unit);

            if (this.isDone()) {
                return this.returnFromResponse();
            } else {
                String msg = getTimeoutMessage(timeout);
                throw new TimeoutException(msg);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void received(Response response) {
        try {
            lock.lock();
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean complete(Object value) {
        if (value instanceof Response) {
            this.received((Response) value);
            return super.complete(returnFromResponse());
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return this.response != null;
    }

    private Object returnFromResponse() {
        if (!isDone()) {
            throw new IllegalStateException("response cannot be null");
        }

        if (ErrorCode.SUCCESS.errorCode == response.getResultCode()) {
            return response.getResult();
        }

        throw new RuntimeException(response.getErrorMsg());
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        Response errorResult = new Response();
        errorResult.setSessionId(request.getSessionId());
        errorResult.setErrorMsg("request future has been canceled.");
        errorResult.setResultCode(ErrorCode.ERROR.errorCode);
        response = errorResult;
        ResponseHolder.remove(request.getSessionId());
        cancelled = true;
        return true;
    }

    private String getTimeoutMessage(long timeout) {
        StringBuilder builder = new StringBuilder();
        for (Class<?> type : request.getParametersType()) {
            builder.append(type.getName()).append(",");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        return String.format("Timeout by: %ss! Class: %s, method: %s(%s), sessionId: %s", timeout,
                request.getClassName(), request.getMethodName(), builder.toString(), request.getSessionId());
    }
}