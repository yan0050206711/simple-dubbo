package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.netty.client.MessageSender;

public class NettyInvoke extends AbstractInvoke {

    @Override
    public Object invoke(RegistryNode nodeInfo, Invocation invocation) throws Exception {

        return MessageSender.send(nodeInfo, invocation);
    }

}
