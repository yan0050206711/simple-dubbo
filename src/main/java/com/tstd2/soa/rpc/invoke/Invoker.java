package com.tstd2.soa.rpc.invoke;

/**
 * 具体的调用者
 */
public interface Invoker {

    Object invoke(Invocation invocation) throws Exception;

}
