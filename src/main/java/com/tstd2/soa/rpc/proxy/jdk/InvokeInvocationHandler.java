package com.tstd2.soa.rpc.proxy.jdk;

import com.tstd2.soa.rpc.cluster.Cluster;
import com.tstd2.soa.config.Reference;
import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeInvocationHandler implements InvocationHandler {

    private Invoke invoke;

    private Reference reference;

    public InvokeInvocationHandler(Invoke invoke, Reference reference) {
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

        Cluster cluster = Reference.getClusters().get(reference.getCluster());
        Object result = cluster.invoke(invocation);

        return result;
    }

}
