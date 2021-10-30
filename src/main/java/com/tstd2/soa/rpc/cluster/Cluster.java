package com.tstd2.soa.rpc.cluster;

import com.tstd2.soa.rpc.invoke.RpcInvocation;

/**
 * 集群容错接口
 */
public interface Cluster {

    Object invoke(RpcInvocation invocation) throws Exception;

}
