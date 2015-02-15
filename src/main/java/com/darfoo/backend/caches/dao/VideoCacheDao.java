package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.CacheInsertProtocol;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zjh on 14-12-17.
 */
public class VideoCacheDao {
    @Autowired
    VideoDao videoDao;
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    CacheInsertProtocol cacheInsertProtocol;

    /**
     * 为单个视频资源进行缓存
     *
     * @param video
     * @return
     */
    public boolean insertSingleVideo(Video video) {
        return cacheInsertProtocol.insertResourceIntoCache(Video.class, video);
    }

    public boolean insertRecommendVideo(Video video) {
        return cacheInsertProtocol.insertResourceIntoCache(Video.class, video);
    }

    public boolean insertMusic(int vid, int mid) {
        String key = "videomusic-" + vid;
        if (!commonRedisClient.exists(key)) {
            commonRedisClient.set(key, mid + "");
            return true;
        } else {
            return false;
        }
    }

    public CacheSingleVideo getSingleVideo(Integer id) {
        String key = "video-" + id;
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String videourl = commonRedisClient.hget(key, "video_url");
        String imageurl = commonRedisClient.hget(key, "image_url");
        String timestamp = commonRedisClient.hget(key, "update_timestamp");
        String type = commonRedisClient.hget(key, "type");
        return new CacheSingleVideo(id, title, authorname, videourl, imageurl, Integer.parseInt(type), Long.parseLong(timestamp));
    }

    public CacheSingleVideo getRecommendVideo(Integer id) {
        String key = "recommendvideo-" + id;
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String videourl = commonRedisClient.hget(key, "video_url");
        String imageurl = commonRedisClient.hget(key, "image_url");
        String timestamp = commonRedisClient.hget(key, "update_timestamp");
        String type = commonRedisClient.hget(key, "type");
        return new CacheSingleVideo(id, title, authorname, videourl, imageurl, Integer.parseInt(type), Long.parseLong(timestamp));
    }
}
