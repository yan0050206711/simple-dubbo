package com.tstd2.soa.rpc.loadbalance;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.registry.RegistryNode;

import java.util.List;
import java.util.Random;

/**
 * 随机
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public NodeInfo deSelect(List<RegistryNode> registryInfo) {
        Random random = new Random();
        int index = random.nextInt(registryInfo.size());
        RegistryNode node = registryInfo.get(index);
        Protocol protocol = node.getProtocol();

        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setHost(protocol.getHost());
        nodeInfo.setPort(protocol.getPort());
        nodeInfo.setContextpath(protocol.getContextpath());

        return nodeInfo;
    }
}
