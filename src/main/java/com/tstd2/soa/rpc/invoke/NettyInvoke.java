package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.netty.client.NettyClient;

public class NettyInvoke extends AbstractInvoke {

    @Override
    public Object doInvoke(Protocol protocol, Request request, int timeout) throws Exception {

        return NettyClient.request(protocol, request, timeout);
    }

}
