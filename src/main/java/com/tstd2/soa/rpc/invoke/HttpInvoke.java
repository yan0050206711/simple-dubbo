package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.http.client.HttpClient;

public class HttpInvoke extends AbstractInvoke  {

    @Override
    Object doInvoke(ProtocolBean protocol, Request request, int timeout) throws Exception {

        return HttpClient.request(protocol, request, timeout);
    }
}
