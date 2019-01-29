package com.tstd2.soa.rpc.loadbalance;

import com.tstd2.soa.registry.RegistryNode;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public RegistryNode deSelect(List<RegistryNode> registryInfo) {
        int size = registryInfo.size();
        int index = 0;
        if (size > 1) {
            index = ThreadLocalRandom.current().nextInt(size);
        }
        RegistryNode node = registryInfo.get(index);

        return node;
    }
}
