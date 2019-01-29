package com.tstd2.soa.rpc.proxy.javassist;

import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.config.Reference;
import com.tstd2.soa.rpc.invoke.Invoke;
import com.tstd2.soa.rpc.proxy.RpcProxy;
import com.tstd2.soa.rpc.proxy.jdk.InvokeInvocationHandler;

public class JavassistProxy implements RpcProxy {

    @Override
    public Object getObject(String className, final Invoke invoke, final Reference reference) throws Exception {

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), ReflectionCache.putAndGetClass(className),
                new InvokeInvocationHandler(invoke, reference));
    }
}
