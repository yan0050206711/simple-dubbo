package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.remoting.exchange.DefaultFuture;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

public class NettyClient {

    /**
     * 通过netty发送消息
     */
    public static Object request(ProtocolUrl protocolUrl, Request request, int timeout) throws Exception {

        boolean isAsync = RpcContext.getContext().isAsync();

        // 通过netty传输管道直接拿到响应结果
        DefaultFuture future = new DefaultFuture(request, isAsync);

        try {
            NettyClientUtil.writeAndFlush(protocolUrl, request);
        } catch (Exception e) {
            future.cancel(true);
            future.completeExceptionally(e);
            throw e;
        }

        if (isAsync) {
            RpcContext.getContext().setFuture(future);
            return null;
        } else {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        }
    }

}
