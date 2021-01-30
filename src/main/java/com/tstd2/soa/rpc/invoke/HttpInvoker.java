package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.http.client.HttpClient;

public class HttpInvoker extends AbstractInvoker {

    @Override
    public Object doInvoke(ProtocolUrl protocolUrl, Request request, int timeout) throws Exception {

        return HttpClient.request(protocolUrl, request, timeout);
    }
}
