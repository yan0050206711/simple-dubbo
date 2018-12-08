package com.tstd2.rpc.netty;

import com.tstd2.rpc.configBean.Service;
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
//        Object clazz = application.getBean(request.getServiceId());
        Object clazz = application.getBean(Class.forName(request.getClassName()));

        // 代理对象里面方法名称和方法参数
        Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParametersType());
        Object result = method.invoke(clazz, request.getParametersValue());
        response.setResult(result);

        // netty异步的方式写回给客户端
        ctx.writeAndFlush(response);
        ctx.close();
    }
}
