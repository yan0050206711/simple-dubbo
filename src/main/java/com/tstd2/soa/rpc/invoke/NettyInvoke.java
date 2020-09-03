package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.netty.client.NettyClient;

public class NettyInvoke extends AbstractInvoke {

    @Override
    public Object doInvoke(ProtocolBean protocol, Request request, int timeout) throws Exception {

        return NettyClient.request(protocol, request, timeout);
    }

}
