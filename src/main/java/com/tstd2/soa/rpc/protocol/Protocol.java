package com.tstd2.soa.rpc.protocol;

import com.tstd2.soa.rpc.invoke.Invoker;

/**
 * @author yancey
 * @date 2021/1/23 16:10
 */
public interface Protocol {

    <T> Invoker refer(Class<T> interfaceClass, Invoker invoker);

}
