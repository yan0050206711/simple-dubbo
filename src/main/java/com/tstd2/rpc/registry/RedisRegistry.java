package com.tstd2.rpc.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tstd2.rpc.configBean.Protocol;
import com.tstd2.rpc.configBean.Registry;
import com.tstd2.rpc.configBean.Service;
import com.tstd2.rpc.redis.RedisClient;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * redis注册中心处理类
 */
public class RedisRegistry implements BaseRegistry {


    private static final Gson GSON = new GsonBuilder().create();

    private RedisClient redisClient;

    @Override
    public boolean registry(String ref, ApplicationContext application) {
        try {
            Protocol protocol = application.getBean(Protocol.class);
            Map<String, Service> services = application.getBeansOfType(Service.class);

            Registry registry = application.getBean(Registry.class);
            this.createRedisPool(registry.getAddress());

            for (Map.Entry<String, Service> entry : services.entrySet()) {
                if (entry.getValue().getRef().equals(ref)) {
                    Map<String, String> map = new HashMap<>(2);
                    map.put("protocol", GSON.toJson(protocol));
                    map.put("service", GSON.toJson(entry.getValue()));

                    Map<String, String> ipport = new HashMap<>(1);
                    ipport.put(protocol.getHost() + ":" + protocol.getPort(), GSON.toJson(map));

                    lpush(ref, ipport);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void lpush(String ref, Map<String, String> ipport) {
        if (redisClient.exists(ref)) {
            Set<String> keys = ipport.keySet();
            String ipportStr = "";
            for (String k : keys) {
                ipportStr = k;
            }

            List<String> registryInfo = this.redisClient.lrange(ref, 0, -1);
            List<String> newRegistry = new ArrayList<>();

            boolean isold = false;

            for (String node : registryInfo) {
                Map<String, String> map = GSON.fromJson(node, Map.class);
                if (map.containsKey(ipportStr)) {
                    newRegistry.add(GSON.toJson(ipport));
                    isold = true;
                }
            }

            if (isold) {
                if (newRegistry.size() > 0) {
                    this.redisClient.del(ref);
                    String[] newReStr = new String[newRegistry.size()];
                    for (int i=0; i<newRegistry.size(); i++) {
                        newReStr[i] = newRegistry.get(i);
                    }
                    this.redisClient.lpush(ref, newReStr);
                }
            }
        }
    }

    /**
     * 初始化redis
     * @param address
     */
    private synchronized void createRedisPool(String address) {

        RedisClient redisClient = new RedisClient();
        // 数据库链接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMinIdle(20);
        config.setMaxWaitMillis(6 * 1000);
        config.setTestOnBorrow(true);

        String[] addrs = address.split(":");
        JedisPool jedisPool = new JedisPool(config, addrs[0], Integer.valueOf(addrs[1]), 5000);
        redisClient.setJedisPool(jedisPool);

        this.redisClient = redisClient;
    }


    @Override
    public List<String> getRegistry(String id, ApplicationContext application) {
        Registry registry = application.getBean(Registry.class);
        this.createRedisPool(registry.getAddress());
        if (this.redisClient.exists(id)) {
            return this.redisClient.lrange(id, 0, -1);
        }
        return null;
    }
}
