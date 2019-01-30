package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.remoting.netty.client.ResponseFuture;

public interface ExchangeClient {

    ResponseFuture request(Object request, int timeout);
}
