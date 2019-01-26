package com.tstd2.soa.registry;

import com.tstd2.soa.config.Registry;
import com.tstd2.soa.registry.redis.RedisClient;
import com.tstd2.soa.registry.redis.RedisFactory;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务注册列表本地缓存
 */
public class RegistryLocalCache {

    private static RedisClient redisClient;

    static  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 监听redis
                 */
                final ApplicationContext applicationContext = Registry.getApplicationContext();
                Registry registry = applicationContext.getBean(Registry.class);
                createRedisPool(registry.getAddress());

                redisClient.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String interfaceName) {
                        List<RegistryNode> registryList = BaseRegistryDelegate.getRegistry(interfaceName, applicationContext);
                        setRegistry(interfaceName, registryList);
                    }
                }, "redis-registry");
            }
        }).start();
    }

    /**
     * 初始化redis
     *
     * @param address
     */
    private static synchronized void createRedisPool(String address) {

        if (redisClient != null) {
            return;
        }

        redisClient = RedisFactory.create(address);
    }

    /**
     * 本地缓存
     */
    private static Map<String, List<RegistryNode>> registryLocalCache = new HashMap<>();

    /**
     * 缓存服务列表
     *
     * @param interfaceName
     * @param registryList
     */
    public synchronized static void setRegistry(final String interfaceName, List<RegistryNode> registryList) {
        registryLocalCache.put(interfaceName, registryList);
    }

    /**
     * 从本地缓存获取服务列表
     *
     * @param interfaceName
     * @return
     */
    public static List<RegistryNode> getRegistry(String interfaceName) {
        return registryLocalCache.get(interfaceName);
    }

}
