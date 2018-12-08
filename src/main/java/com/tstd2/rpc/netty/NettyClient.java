package com.tstd2.rpc.netty;

import com.tstd2.rpc.invoke.Invocation;
import com.tstd2.rpc.loadbalance.NodeInfo;
import com.tstd2.rpc.netty.handler.NettyServerInHandler;
import com.tstd2.rpc.netty.model.Request;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.UUID;

public class NettyClient {

    /**
     * netty channel池
     */
    private static final NettyChannelPool nettyChannelPool = new NettyChannelPool();

    /**
     * 通过netty发送消息
     */
    public static Object sendMsg(NodeInfo nodeInfo, Invocation invocation) throws Exception {
        Request request = new Request();
        request.setSessionId(UUID.randomUUID().toString());
        request.setClassName(invocation.getReference().getInf());
        request.setMethodName(invocation.getMethod().getName());
        request.setParametersType(invocation.getMethod().getParameterTypes());
        request.setParametersValue(invocation.getObjs());

        // 通过netty传输管道直接拿到响应结果
        CallBack callBack = new CallBack();
        CallBackHolder.put(request.getSessionId(), callBack);
        Channel channel = nettyChannelPool.syncGetChannel(nodeInfo.getHost(), Integer.parseInt(nodeInfo.getPort()));
        channel.writeAndFlush(request);

        return callBack.start();

    }

}
