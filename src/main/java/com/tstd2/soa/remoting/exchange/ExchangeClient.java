package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.remoting.exchange.model.Request;

public interface ExchangeClient {

    DefaultFuture request(ProtocolBean protocol, Request request, int timeout);
}
