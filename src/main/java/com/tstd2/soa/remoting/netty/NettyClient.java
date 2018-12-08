package com.tstd2.soa.remoting.netty;

import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.loadbalance.NodeInfo;
import com.tstd2.soa.remoting.netty.model.Request;
import io.netty.channel.*;

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

        int timeout = Integer.parseInt(invocation.getReference().getTimeout());
        return callBack.start(timeout);

    }

}
