package com.tstd2.soa.rpc.proxy.javassist;

import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.invoke.Invoker;
import com.tstd2.soa.rpc.proxy.RpcProxy;
import com.tstd2.soa.rpc.proxy.InvokeInvocationHandler;

public class JavassistProxy implements RpcProxy {

    @Override
    public Object getObject(String className, final Invoker invoke, final ReferenceBean reference) throws Exception {

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), ReflectionCache.putAndGetClass(className),
                new InvokeInvocationHandler(invoke, reference));
    }
}
