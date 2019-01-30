package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.http.client.HttpClient;

public class HttpInvoke extends AbstractInvoke  {

    @Override
    Object invoke(RegistryNode nodeInfo, Invocation invocation) throws Exception {

        return HttpClient.request(nodeInfo, invocation);
    }
}
