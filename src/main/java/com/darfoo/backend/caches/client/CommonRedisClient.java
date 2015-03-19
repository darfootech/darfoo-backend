package com.darfoo.backend.caches.client;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;

/**
 * Created by zjh on 14-12-18.
 */
public class CommonRedisClient extends AbstractBaseRedisDao<String, String> {
    /**
     * 向sorted set中插入值以及这个值对应的权重 这个权重就是用来排序的
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zadd(String key, String value, Double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 从sorted set中取出所有的值
     *
     * @param key
     * @param start
     * @param end
     * @param withScore
     * @return
     */
    public Set zrange(String key, Long start, Long end, Boolean withScore) {
        if (withScore != null && withScore) {
            return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        }
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 从sorted set中逆序取出所有的值
     *
     * @param key
     * @param start
     * @param end
     * @param withScore
     * @return
     */
    public Set zrevrange(String key, Long start, Long end, Boolean withScore) {
        if (withScore != null && withScore) {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        }
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 单键值队存储
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 按键值对存入
     *
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
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return redisTemplate.<String, String>opsForHash().get(key, field);
    }

    /**
     * 根据键来一次性获取和这个键指向的所有键值对
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        return redisTemplate.<String, String>opsForHash().entries(key);
    }

    /**
     * 判断是否有key
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除某一个键对应的记录
     *
     * @param key
     */
    public void delete(String key) {
        List<String> list = new ArrayList<String>();
        list.add(key);
        delete(list);
    }

    /**
     * 删除一系列键对应的记录
     *
     * @param keys
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 像某一个key指向的set中插入内容
     *
     * @param key
     * @param value
     * @return
     */
    public Long sadd(String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取某一个key指向的set中的所有内容
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 向某一个key指向的list尾部中插入数据
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 向某一个key指向的list头部中插入数据
     *
     * @param key
     * @param value
     * @return
     */
    public Long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从key对应的list中按照首尾位置来取出元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 判断某一个key对应的redis集合中是否存在某一个field
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 清空redis缓存
     *
     * @return
     */
    public boolean deleteCurrentDB() {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.flushDb();
                return true;
            }
        });
    }
}
