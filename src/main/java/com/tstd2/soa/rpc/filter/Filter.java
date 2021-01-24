package com.tstd2.soa.rpc.filter;

import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoker;

/**
 * @author yancey
 * @date 2021/1/23 16:02
 */
public interface Filter {

    Object invoke(Invoker invoker, Invocation invocation);
}
