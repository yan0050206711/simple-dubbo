package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
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
        return ResponseClientUtil.response(request, response, ctx);
    }

}
