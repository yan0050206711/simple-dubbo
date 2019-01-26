package com.tstd2.soa.registry.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {

    /**
     * 初始化redis
     *
     * @param address
     */
    public static RedisClient create(String address) {

        RedisClient redisClient = new RedisClient();
        // 数据库链接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMinIdle(20);
        config.setMaxWaitMillis(5 * 1000);
        config.setTestOnBorrow(true);

        String[] addrs = address.split(":");
        JedisPool jedisPool = new JedisPool(config, addrs[0], Integer.valueOf(addrs[1]), 5000);
        redisClient.setJedisPool(jedisPool);

        return redisClient;
    }

}
