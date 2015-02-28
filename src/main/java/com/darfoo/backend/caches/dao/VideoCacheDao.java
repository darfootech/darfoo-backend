package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.cota.CacheProtocol;
import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjh on 14-12-17.
 */
public class VideoCacheDao {
    @Autowired
    CommonRedisClient commonRedisClient;

    public boolean insertMusic(int vid, int mid) {
        String key = "videomusic-" + vid;
        if (!commonRedisClient.exists(key)) {
            commonRedisClient.set(key, mid + "");
            return true;
        } else {
            return false;
        }
    }
}
