package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.netty.client.NettyClient;

public class NettyInvoker extends AbstractInvoker {

    @Override
    public Object doInvoke(RegistryNode.ProtocolUrl protocol, Request request, int timeout) throws Exception {

        return NettyClient.request(protocol, request, timeout);
    }

}
