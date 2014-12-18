package com.darfoo.backend.caches;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by zjh on 14-12-18.
 */
public class CommonRedisClient extends AbstractBaseRedisDao<String, String>{
    /**
     * 按键值对存入
     * @param key
     * @param param
     */
    public void hmset(String key, HashMap param) {
        redisTemplate.opsForHash().putAll(key, param);
    }

    public Collection<String> hmget(String key, Collection<String> fields) {
        return redisTemplate.<String, String>opsForHash().multiGet(key, fields);
    }

    /**
     * 按照键和字段得到值
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return redisTemplate.<String, String>opsForHash().get(key, field);
    }

    /**
     * 判断是否有key
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
