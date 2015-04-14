package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.client.CommonRedisClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zjh on 15-4-4.
 */
public class StatisticsCacheDao {
    @Autowired
    CommonRedisClient redisClient;

    public void insertHotSearchIntoCache(List keywords, String type) {
        for (Object keyword : keywords) {
            redisClient.zadd(String.format("%shotsearch", type), (String) keyword, 3.0);
        }
    }

    public List extractHotSearchFromCache(String type) {
        Collection<String> keys;
        keys = redisClient.zrevrange(String.format("%shotsearch", type), 0L, -1L, false);

        List result = new ArrayList();
        for (String key : keys) {
            result.add(key);
        }
        return result;
    }
}
