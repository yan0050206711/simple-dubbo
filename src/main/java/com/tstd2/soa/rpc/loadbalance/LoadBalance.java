package com.tstd2.soa.rpc.loadbalance;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalance {

    RegistryNode deSelect(List<RegistryNode> registryInfo);

}
