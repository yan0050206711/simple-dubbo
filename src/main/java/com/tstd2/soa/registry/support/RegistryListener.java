package com.tstd2.soa.registry.support;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

public class RegistryListener implements NotifyListener {

    private String interfaceName;

    public RegistryListener(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public void notify(List<RegistryNode> registryNodes) {
        RegistryLocalCache.setRegistry(interfaceName, registryNodes);
    }

}
