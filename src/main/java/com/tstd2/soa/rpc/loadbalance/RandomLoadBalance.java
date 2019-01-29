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
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int index = random.nextInt(registryInfo.size());
        RegistryNode node = registryInfo.get(index);

        return node;
    }
}
