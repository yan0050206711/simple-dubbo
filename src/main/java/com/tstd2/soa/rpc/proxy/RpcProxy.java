package com.tstd2.soa.rpc.proxy;

import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.invoke.Invoke;

public interface RpcProxy {

    Object getObject(String className, Invoke invoke,  ReferenceBean reference) throws Exception;

}
