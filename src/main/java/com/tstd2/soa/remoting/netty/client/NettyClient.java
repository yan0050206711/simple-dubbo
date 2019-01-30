package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.remoting.exchange.model.Request;

public class NettyClient {

    /**
     * 通过netty发送消息
     */
    public static Object request(Protocol protocol, Request request, int timeout) throws Exception {

        // 通过netty传输管道直接拿到响应结果
        ResponseFuture future = new ResponseFuture(request);

        try {
            NettyClientUtil.writeAndFlush(protocol, request);
        } catch (Exception e) {
            future.cancel();
            throw e;
        }

        return future.start(timeout);

    }

}
