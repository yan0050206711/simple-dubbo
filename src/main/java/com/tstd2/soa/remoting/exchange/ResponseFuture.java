package com.tstd2.soa.remoting.exchange;

import com.tstd2.soa.remoting.exchange.model.Response;

import java.util.concurrent.Future;

/**
 * @author yancey
 * @date 2020/9/3 21:54
 */
public interface ResponseFuture extends Future {

    void received(Response response);

}
