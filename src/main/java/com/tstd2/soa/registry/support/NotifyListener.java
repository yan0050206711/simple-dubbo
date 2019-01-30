package com.tstd2.soa.registry.support;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

public interface NotifyListener {

    /**
     * 每次通知都是全量通知
     *
     * @param registryNodes
     */
    void notify(List<RegistryNode> registryNodes);

}