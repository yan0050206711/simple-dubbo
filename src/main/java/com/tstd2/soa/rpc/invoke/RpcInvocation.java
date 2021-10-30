package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.ReferenceBean;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RpcInvocation implements Invocation, Serializable {
    private static final long serialVersionUID = -418511924177230752L;

    private Method method;
    private Object[] objs;
    private Invoker invoke;
    private ReferenceBean reference;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getObjs() {
        return objs;
    }

    public void setObjs(Object[] objs) {
        this.objs = objs;
    }

    public Invoker getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoker invoke) {
        this.invoke = invoke;
    }

    public ReferenceBean getReference() {
        return reference;
    }

    public void setReference(ReferenceBean reference) {
        this.reference = reference;
    }

    @Override
    public String getClassName() {
        return reference.getInf();
    }

    @Override
    public String getMethodName() {
        return method.getName();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object[] getArguments() {
        return objs;
    }

}
