package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.remoting.exchange.DefaultFuture;
import com.tstd2.soa.remoting.exchange.model.Request;

public class NettyClient {

    /**
     * 通过netty发送消息
     */
    public static Object request(ProtocolBean protocol, Request request, int timeout) throws Exception {

        // 通过netty传输管道直接拿到响应结果
        DefaultFuture future = new DefaultFuture(request);

        try {
            NettyClientUtil.writeAndFlush(protocol, request);
        } catch (Exception e) {
            future.cancel();
            throw e;
        }

        return future.get(timeout);

    }

}
