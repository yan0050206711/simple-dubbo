package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.remoting.exchange.ResponseFuture;
import com.tstd2.soa.remoting.exchange.ResponseHolder;
import com.tstd2.soa.remoting.exchange.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取远程服务端信息包
        Response response = (Response) msg;

        Long sessionId = response.getSessionId();

        ResponseFuture future = ResponseHolder.get(sessionId);
        if (future != null) {
            future.over(response);
        }
    }

}
