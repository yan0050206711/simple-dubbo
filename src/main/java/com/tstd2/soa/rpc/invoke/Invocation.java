package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.ReferenceBean;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Invocation implements Serializable {
    private static final long serialVersionUID = -418511924177230752L;

    private Method method;
    private Object[] objs;
    private Invoke invoke;
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

    public Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public ReferenceBean getReference() {
        return reference;
    }

    public void setReference(ReferenceBean reference) {
        this.reference = reference;
    }

}
