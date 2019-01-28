package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.netty.model.Request;
import com.tstd2.soa.rpc.invoke.Invocation;

import java.util.UUID;

public class MessageSender {

    /**
     * 通过netty发送消息
     */
    public static Object send(RegistryNode nodeInfo, Invocation invocation) throws Exception {
        final Request request = new Request();
        request.setSessionId(UUID.randomUUID().toString());
        request.setClassName(invocation.getReference().getInf());
        request.setMethodName(invocation.getMethod().getName());
        request.setParametersType(invocation.getMethod().getParameterTypes());
        request.setParametersValue(invocation.getObjs());

        // 通过netty传输管道直接拿到响应结果
        ResponseFuture callBack = new ResponseFuture();
        ResponseHolder.put(request.getSessionId(), callBack);
        NettyClient.writeAndFlush(nodeInfo.getProtocol(), request);

        // 默认用客户端配置的超时时间，客户端没有配置超时时间则用提供方的
        Integer timeout = Integer.parseInt(invocation.getReference().getTimeout());
        if (timeout == null || timeout == 0) {
            timeout = Integer.parseInt(nodeInfo.getService().getTimeout());
        }

        return callBack.start(timeout);

    }

}
