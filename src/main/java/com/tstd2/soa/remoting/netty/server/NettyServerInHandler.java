package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.config.Service;
import com.tstd2.soa.remoting.netty.model.Request;
import com.tstd2.soa.remoting.netty.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 反序列化
        Request request = (Request) msg;
        Response response = new Response();

        // 从spring服务实例对象
        ApplicationContext application = Protocol.getApplicationContext();
        // 拿到服务端协议配置
        Protocol protocol = application.getBean(Protocol.class);

        int threads = Integer.parseInt(protocol.getThreads());

        // 不阻塞nio线程，复杂的业务逻辑丢给专门的业务线程池
        // 复用work线程池？ctx.executor()
        ServiceExecutor.submit(new ServiceTask(request, response, ctx), threads, protocol);
    }
}
