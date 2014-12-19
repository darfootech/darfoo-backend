package com.darfoo.backend.service;

import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zjh on 14-12-18.
 */

@Controller
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    MusicCacheDao musicCacheDao;

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo getSingleVideoFromCache(@PathVariable String id){
        Integer vid = Integer.parseInt(id);
        return videoCacheDao.getSingleVideo(vid);
    }

    @RequestMapping(value = "/tutorial/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo getSingleTutorialFromCache(@PathVariable String id){
        Integer tid = Integer.parseInt(id);
        return tutorialCacheDao.getSingleTutorial(tid);
    }

    @RequestMapping(value = "/music/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleMusic getSingleMusicFromCache(@PathVariable String id){
        Integer mid = Integer.parseInt(id);
        return musicCacheDao.getSingleMusic(mid);
    }
}
