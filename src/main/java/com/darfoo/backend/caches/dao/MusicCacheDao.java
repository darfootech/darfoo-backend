package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CacheProtocol;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 14-12-18.
 */
public class MusicCacheDao extends AbstractBaseRedisDao<String, Music> {
    @Autowired
    CacheProtocol cacheProtocol;

    public boolean insertSingleMusic(Music music){
        return cacheProtocol.insertResourceIntoCache(Music.class, music, "music");
    }

    public SingleMusic getSingleMusic(Integer id){
        return (SingleMusic) cacheProtocol.extractResourceFromCache(SingleMusic.class, id, "music");
    }
}
