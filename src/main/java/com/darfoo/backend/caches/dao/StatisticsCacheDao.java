package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.service.cota.CacheCollType;
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

    String hotsearchCachekey = "hotsearch";

    public void insertHotSearchIntoCache(List keywords) {
        for (Object keyword : keywords) {
            redisClient.zadd(hotsearchCachekey, (String) keyword, 3.0);
        }
    }

    public List extractHotSearchFromCache() {
        Collection<String> keys;
        keys = redisClient.zrevrange(hotsearchCachekey, 0L, -1L, false);

        List result = new ArrayList();
        for (String key : keys) {
            result.add(key);
        }
        return result;
    }
}
