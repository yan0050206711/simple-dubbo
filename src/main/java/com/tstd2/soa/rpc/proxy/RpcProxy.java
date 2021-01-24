package com.tstd2.soa.rpc.proxy;

import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.invoke.Invoker;

public interface RpcProxy {

    Object getObject(String className, Invoker invoke, ReferenceBean reference) throws Exception;

}
