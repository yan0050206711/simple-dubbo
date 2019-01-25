package com.tstd2.soa.remoting.netty.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 服务端超时检测
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.ALL_IDLE.equals(event.state())) {
                System.out.println("已经10秒没有接收到客户端的信息了，关闭这个链接");
                ctx.writeAndFlush("heart");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    }

}
