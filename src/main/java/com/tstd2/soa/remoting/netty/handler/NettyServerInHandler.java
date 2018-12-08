package com.tstd2.soa.remoting.netty.handler;

import com.tstd2.soa.config.Service;
import com.tstd2.soa.remoting.netty.model.Request;
import com.tstd2.soa.remoting.netty.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 反序列化
        Request request = (Request) msg;

        Response response = new Response();
        response.setSessionId(request.getSessionId());

        // 从spring服务实例对象
        ApplicationContext application = Service.getApplicationContext();
        Class<?> clazz = Class.forName(request.getClassName());
        Object serviceBean = application.getBean(clazz);

        // 代理对象里面方法名称和方法参数
        Method method = clazz.getMethod(request.getMethodName(), request.getParametersType());
        Object result = method.invoke(serviceBean, request.getParametersValue());
        response.setResult(result);

        // netty异步的方式写回给客户端
        ctx.writeAndFlush(response);
    }
}
