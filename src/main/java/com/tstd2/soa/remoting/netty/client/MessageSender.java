package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.remoting.netty.model.Request;
import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.loadbalance.NodeInfo;

import java.util.UUID;

public class MessageSender {

    /**
     * 通过netty发送消息
     */
    public static Object send(NodeInfo nodeInfo, Invocation invocation) throws Exception {
        final Request request = new Request();
        request.setSessionId(UUID.randomUUID().toString());
        request.setClassName(invocation.getReference().getInf());
        request.setMethodName(invocation.getMethod().getName());
        request.setParametersType(invocation.getMethod().getParameterTypes());
        request.setParametersValue(invocation.getObjs());

        // 通过netty传输管道直接拿到响应结果
        CallBack callBack = new CallBack();
        CallBackHolder.put(request.getSessionId(), callBack);
        NettyClient.writeAndFlush(nodeInfo, request);

        int timeout = Integer.parseInt(invocation.getReference().getTimeout());
        return callBack.start(timeout);

    }

}
