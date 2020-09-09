package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.http.client.HttpClient;

public class HttpInvoke extends AbstractInvoke  {

    @Override
    public Object doInvoke(RegistryNode.ProtocolUrl protocol, Request request, int timeout) throws Exception {

        return HttpClient.request(protocol, request, timeout);
    }
}
