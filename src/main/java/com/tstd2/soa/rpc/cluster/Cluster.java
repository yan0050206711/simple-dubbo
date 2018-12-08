package com.tstd2.soa.rpc.cluster;

import com.tstd2.soa.rpc.invoke.Invocation;

/**
 * 集群容错接口
 */
public interface Cluster {

    Object invoke(Invocation invocation) throws Exception;

}
