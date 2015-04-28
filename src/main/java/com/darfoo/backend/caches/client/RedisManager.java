package com.darfoo.backend.caches.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zjh on 15-4-28.
 */
public class RedisManager {
    public static JedisPool jedisPool; // 池化管理jedis链接池
    public static Config redisConfig;

    static {
        redisConfig = ConfigFactory.load("redis");
    }

    public static JedisPool getRedisPoolInstance() {
        if (jedisPool == null) {
            int maxActive = redisConfig.getInt("redis.maxActive");
            int maxIdle = redisConfig.getInt("redis.maxIdle");
            int maxWait = redisConfig.getInt("redis.maxWait");
            int timeout = redisConfig.getInt("redis.timeout");
            int port = redisConfig.getInt("redis.port");
            String host = redisConfig.getString("redis.host");
            String password = redisConfig.getString("redis.pass");
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxActive);
            config.setMaxIdle(maxIdle);
            config.setMaxWaitMillis(maxWait);
            jedisPool = new JedisPool(config, host, port, timeout, password);
        }
        return jedisPool;
    }
}
