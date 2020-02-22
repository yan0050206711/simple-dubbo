package com.tstd2.soa.rpc.loadbalance;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

/**
 * 轮询
 */
public class RoundrobinLoadBalance implements LoadBalance {
    @Override
    public RegistryNode deSelect(List<RegistryNode> registryInfo) {
        return null;
    }
}
