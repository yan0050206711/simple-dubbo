package com.tstd2.rpc.invoke;

import com.tstd2.rpc.configBean.Reference;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Invocation implements Serializable {
    private static final long serialVersionUID = -418511924177230752L;

    private Method method;
    private Object[] objs;
    private Invoke invoke;
    private Reference reference;

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

    public Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }
}
