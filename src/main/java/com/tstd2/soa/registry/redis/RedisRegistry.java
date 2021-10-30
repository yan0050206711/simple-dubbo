package com.tstd2.soa.registry.redis;

import com.tstd2.soa.common.JsonUtils;
import com.tstd2.soa.config.RegistryBean;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.registry.BaseRegistry;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.support.NotifyListener;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * redis注册中心处理类
 */
public class RedisRegistry implements BaseRegistry {

    private RedisClient redisClient;

    private String channel = "redis-registry";

    @Override
    public boolean registry(String interfaceName, RegistryNode registryNode) {
        try {
            RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
            this.createRedisPool(registry.getAddress());

            // 更新redis
            this.sadd(interfaceName, registryNode);

            // 发出通知
            this.redisClient.publish(channel, interfaceName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unregister(String interfaceName, RegistryNode registryNode) {

    }

    @Override
    public void subscribe(String interfaceName, NotifyListener listener) {
        new Thread(() -> redisClient.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                listener.notify(getRegistry(interfaceName));
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                listener.notify(getRegistry(interfaceName));
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                listener.notify(getRegistry(interfaceName));
            }
        }, channel)).start();
    }

    @Override
    public void unsubscribe(String interfaceName, NotifyListener listener) {

    }

    private void sadd(String interfaceName, RegistryNode registryNode) {
        if (redisClient.exists(interfaceName)) {

            String host = registryNode.getProtocol().getHost();
            String port = registryNode.getProtocol().getPort();

            Set<String> nodeSet = this.redisClient.smembers(interfaceName);

            for (String nodeJson : nodeSet) {
                RegistryNode node = JsonUtils.fromJson(nodeJson, RegistryNode.class);

                // 是有有相同的机器，则删除后更新
                if (host.equals(node.getProtocol().getHost()) && port.equals(node.getProtocol().getPort())) {
                    // 存在则更新
                    this.redisClient.srem(interfaceName, nodeJson);
                    break;
                }
            }
        }

        this.redisClient.sadd(interfaceName, JsonUtils.toJson(registryNode));

    }

    /**
     * 初始化redis
     *
     * @param address
     */
    private synchronized void createRedisPool(String address) {

        if (this.redisClient != null) {
            return;
        }

        this.redisClient = RedisFactory.create(address);
    }

    @Override
    public List<RegistryNode> getRegistry(String interfaceName) {
        RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
        this.createRedisPool(registry.getAddress());
        if (this.redisClient.exists(interfaceName)) {
            Set<String> set = this.redisClient.smembers(interfaceName);
            List<RegistryNode> nodeList = new ArrayList<>();
            for (String str : set) {
                nodeList.add(JsonUtils.fromJson(str, RegistryNode.class));
            }
            return nodeList;
        }
        return null;
    }

}
