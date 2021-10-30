package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoker;
import com.tstd2.soa.rpc.protocol.ProtocolFilterWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 反序列化
        Request request = (Request) msg;
        Response response = new Response();

        Invoker invoker = new ProtocolFilterWrapper().export(new Invoker() {
            @Override
            public Object invoke(Invocation invocation) throws Exception {

                // 从spring服务实例对象
                // 拿到服务端协议配置
                ProtocolBean protocol = SpringContextHolder.getBean(ProtocolBean.class);

                int threads = Integer.parseInt(protocol.getThreads());

                // 不阻塞nio线程，复杂的业务逻辑丢给专门的业务线程池
                // 复用work线程池？ctx.executor()
                ServiceExecutor.submit(new ServiceTask(request, response, ctx), threads, protocol);

                return null;
            }
        });

        invoker.invoke(request);

    }

}
