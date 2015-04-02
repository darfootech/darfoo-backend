package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjh on 14-12-17.
 */
public class AccompanyCacheDao {
    @Autowired
    CommonRedisClient commonRedisClient;

    public boolean insertAccompany(Class resource, Class accompany, int resourceid, int accompanyid) {
        String key = String.format("%s%s-%d", resource.getSimpleName().toLowerCase(), accompany.getSimpleName().toLowerCase(), resourceid);
        if (!commonRedisClient.exists(key)) {
            commonRedisClient.set(key, accompanyid + "");
            return true;
        } else {
            return false;
        }
    }
}
