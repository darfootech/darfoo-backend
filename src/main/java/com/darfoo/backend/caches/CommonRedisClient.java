package com.darfoo.backend.caches;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;

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

    /**
     * 删除某一个键对应的记录
     * @param key
     */
    public void delete(String key) {
        List<String> list = new ArrayList<String>();
        list.add(key);
        delete(list);
    }

    /**
     * 删除一系列键对应的记录
     * @param keys
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 像某一个key指向的set中插入内容
     * @param key
     * @param value
     * @return
     */
    public Long sadd(String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取某一个key指向的set中的所有内容
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 清空redis缓存
     * @return
     */
    public boolean deleteCurrentDB(){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.flushDb();
                return true;
            }
        });
    }
}
