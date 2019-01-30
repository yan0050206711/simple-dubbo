package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import com.tstd2.soa.remoting.netty.ResponseClientUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Callable;

public class ServiceTask implements Callable<Object> {

    private Request request;
    private Response response;
    private ChannelHandlerContext ctx;

    public ServiceTask(Request request, Response response, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.ctx = ctx;
    }

    @Override
    public Object call() throws Exception {
        response = ResponseClientUtil.response(request, response);

        // netty异步的方式写回给客户端
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                System.out.println("RPC Server Send response sessionId:" + request.getSessionId());
            }
        });

        return true;

    }

}
