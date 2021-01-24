package com.tstd2.soa.rpc.proxy.jdk;

import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.invoke.Invoker;
import com.tstd2.soa.rpc.proxy.InvokeInvocationHandler;
import com.tstd2.soa.rpc.proxy.RpcProxy;

import java.lang.reflect.Proxy;

public class JdkProxy implements RpcProxy {
    @Override
    public Object getObject(String className, Invoker invoke, ReferenceBean reference) throws Exception {

        // 生成一个代理对象
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{ReflectionCache.putAndGetClass(className)},
                new InvokeInvocationHandler(invoke, reference));
    }
}
