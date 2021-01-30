package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.netty.client.NettyClient;

public class NettyInvoker extends AbstractInvoker {

    @Override
    public Object doInvoke(ProtocolUrl protocolUrl, Request request, int timeout) throws Exception {

        return NettyClient.request(protocolUrl, request, timeout);
    }

}
