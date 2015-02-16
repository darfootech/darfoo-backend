package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CacheProtocol;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 14-12-18.
 */
public class TutorialCacheDao extends AbstractBaseRedisDao<String, Tutorial> {
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    CacheProtocol cacheProtocol;

    public boolean insertSingleTutorial(Tutorial tutorial){
        return cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "tutorial");
    }

    public boolean insertRecommendTutorial(Tutorial tutorial){
        return cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "recommendvideo");
    }

    public CacheSingleVideo getSingleTutorial(Integer id){
        return (CacheSingleVideo) cacheProtocol.extractResourceFromCache(CacheSingleVideo.class, id, "tutorial");
    }
}
