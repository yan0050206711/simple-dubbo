package com.tstd2.soa.rpc.proxy;

import com.tstd2.soa.config.ProtocolConfig;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.cluster.Cluster;
import com.tstd2.soa.rpc.invoke.RpcInvocation;
import com.tstd2.soa.rpc.invoke.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeInvocationHandler implements InvocationHandler {

    private Invoker invoke;
    private ReferenceBean reference;

    public InvokeInvocationHandler(Invoker invoke, ReferenceBean reference) {
        this.invoke = invoke;
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 在这个invoke里面最终要调用多个远程的provider
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethod(method);
        invocation.setObjs(args);
        invocation.setReference(reference);
        invocation.setInvoke(invoke);

        Cluster cluster = ProtocolConfig.clusters.get(reference.getCluster());
        Object result = cluster.invoke(invocation);

        return result;
    }

}
