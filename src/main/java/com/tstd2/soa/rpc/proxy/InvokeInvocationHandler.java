package com.tstd2.soa.rpc.proxy;

import com.tstd2.soa.rpc.cluster.Cluster;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.rpc.invoke.Invocation;
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
        Invocation invocation = new Invocation();
        invocation.setMethod(method);
        invocation.setObjs(args);
        invocation.setReference(reference);
        invocation.setInvoke(invoke);

        Cluster cluster = ReferenceBean.getClusters().get(reference.getCluster());
        Object result = cluster.invoke(invocation);

        return result;
    }

}
