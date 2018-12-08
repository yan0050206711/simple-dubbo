package com.tstd2.soa.rpc.cluster;

import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoke;

/**
 * 直接忽略
 */
public class FailsafeClusterInvoke implements Cluster {
    @Override
    public Object invoke(Invocation invocation) throws Exception {
        Invoke invoke = invocation.getInvoke();

            try {
                Object result = invoke.invoke(invocation);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "忽略";
            }
    }
}
