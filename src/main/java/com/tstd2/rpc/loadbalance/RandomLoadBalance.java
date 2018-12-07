package com.tstd2.rpc.loadbalance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tstd2.rpc.configBean.Protocol;
import com.tstd2.rpc.configBean.Registry;

import java.util.List;
import java.util.Random;

/**
 * 随机
 */
public class RandomLoadBalance implements LoadBalance {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public NodeInfo deSelect(List<String> registryInfo) {
        Random random = new Random();
        int index = random.nextInt(registryInfo.size());
        String registry = registryInfo.get(index);

        Registry registryObj = GSON.fromJson(registry, Registry.class);

        Protocol protocol = GSON.fromJson(registryObj.getProtocol(), Protocol.class);

        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setHost(protocol.getHost());
        nodeInfo.setPort(protocol.getPort());
        nodeInfo.setContextpath(protocol.getContextpath());

        return nodeInfo;
    }
}
