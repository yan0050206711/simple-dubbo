package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.remoting.exchange.model.Request;

public interface ExchangeClient {

    ResponseFuture request(Protocol protocol, Request request, int timeout);
}
