package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.remoting.netty.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取远程服务端信息包
        Response response = (Response) msg;

        String sessionId = response.getSessionId();

        CallBack callBack = CallBackHolder.get(sessionId);
        if (callBack != null) {
            CallBackHolder.remove(sessionId);
            callBack.over(response);
        }
    }

}
