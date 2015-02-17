package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CacheProtocol;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjh on 14-12-18.
 */
public class TutorialCacheDao extends AbstractBaseRedisDao<String, Tutorial> {
    @Autowired
    CacheProtocol cacheProtocol;

    public boolean insertSingleTutorial(Tutorial tutorial) {
        return cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "tutorial");
    }

    public boolean insertRecommendTutorial(Tutorial tutorial) {
        return cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "recommendvideo");
    }

    public SingleVideo getSingleTutorial(Integer id) {
        return (SingleVideo) cacheProtocol.extractResourceFromCache(SingleVideo.class, id, "tutorial");
    }
}
