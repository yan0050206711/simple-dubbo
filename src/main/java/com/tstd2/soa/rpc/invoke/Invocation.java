package com.tstd2.soa.rpc.invoke;

public interface Invocation {

    String getClassName();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

}