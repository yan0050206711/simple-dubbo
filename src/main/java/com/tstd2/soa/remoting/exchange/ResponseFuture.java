package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.remoting.exchange.model.Response;

/**
 * @author yancey
 * @date 2020/9/3 21:54
 */
public interface ResponseFuture {

    Object get() throws Exception;

    Object get(int timeout) throws Exception;

    void received(Response response);

}
