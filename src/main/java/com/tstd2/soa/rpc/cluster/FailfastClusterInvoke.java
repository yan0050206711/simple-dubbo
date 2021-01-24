package com.tstd2.soa.rpc.cluster;

import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoker;

/**
 * 直接失败
 */
public class FailfastClusterInvoke implements Cluster {
    @Override
    public Object invoke(Invocation invocation) throws Exception {
        Invoker invoke = invocation.getInvoke();

            try {
                Object result = invoke.invoke(invocation);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
    }
}
