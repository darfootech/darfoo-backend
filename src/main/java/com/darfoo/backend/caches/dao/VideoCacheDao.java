package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.CacheProtocol;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 14-12-17.
 */
public class VideoCacheDao {
    @Autowired
    VideoDao videoDao;
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    CacheProtocol cacheProtocol;

    /**
     * 为单个视频资源进行缓存
     *
     * @param video
     * @return
     */
    public boolean insertSingleVideo(Video video) {
        return cacheProtocol.insertResourceIntoCache(Video.class, video, "video");
    }

    public boolean insertRecommendVideo(Video video) {
        return cacheProtocol.insertResourceIntoCache(Video.class, video, "recommendvideo");
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
        return (CacheSingleVideo) cacheProtocol.extractResourceFromCache(Video.class, CacheSingleVideo.class, id, "video");
    }

    public CacheSingleVideo getRecommendVideo(Integer id) {
        return (CacheSingleVideo) cacheProtocol.extractResourceFromCache(Video.class, CacheSingleVideo.class, id, "recommendvideo");
    }
}
