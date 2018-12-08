package com.tstd2.rpc.loadbalance;

import com.tstd2.rpc.registry.RegistryNode;

import java.util.List;

public class RoundrobLoadBalance implements LoadBalance {
    @Override
    public NodeInfo deSelect(List<RegistryNode> registryInfo) {
        return null;
    }
}
