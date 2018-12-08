package com.tstd2.soa.rpc.loadbalance;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

public class RoundrobLoadBalance implements LoadBalance {
    @Override
    public NodeInfo deSelect(List<RegistryNode> registryInfo) {
        return null;
    }
}
