package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.netty.client.ResponseFuture;

public interface ExchangeClient {

    ResponseFuture request(Protocol protocol, Request request, int timeout);
}
