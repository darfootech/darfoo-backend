package com.darfoo.backend.service;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.service.responsemodel.VideoCates;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-18.
 */

@Controller
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    CommonRedisClient redisClient;

    VideoCates videoCates = new VideoCates();

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleVideoFromCache(@PathVariable String id) {
        Integer vid = Integer.parseInt(id);
        return videoCacheDao.getSingleVideo(vid);
    }

    @RequestMapping(value = "/tutorial/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleTutorialFromCache(@PathVariable String id) {
        Integer tid = Integer.parseInt(id);
        return tutorialCacheDao.getSingleTutorial(tid);
    }

    @RequestMapping(value = "/music/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleMusic getSingleMusicFromCache(@PathVariable String id) {
        Integer mid = Integer.parseInt(id);
        return musicCacheDao.getSingleMusic(mid);
    }

    @RequestMapping(value = "/video/recommend", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> cacheRecmmendVideos() {
        List<Video> recommendVideos = videoDao.getRecommendVideos(7);
        for (Video video : recommendVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videorecommend", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        Set<String> recommendVideoKeys = redisClient.smembers("videorecommend");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : recommendVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }
        return result;
    }


    @RequestMapping(value = "/video/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> cacheIndexVideos() {
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
        for (Video video : latestVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videoindex", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        Set<String> latestVideoKeys = redisClient.smembers("videoindex");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : latestVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/video/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getVideosByCategories(@PathVariable String categories) {
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")) {
            String speedCate = videoCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")) {
            String difficultyCate = videoCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
        }
        if (!requestCategories[2].equals("0")) {
            String styleCate = videoCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[3].equals("0")) {
            String letterCate = requestCategories[3];
            targetCategories.add(letterCate);
        }

        //System.out.println(targetCategories.toString());

        List<Video> targetVideos = videoDao.getVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        for (Video video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videocategory"+categories, "video-"+vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }

        Set<String> categoryVideoKeys = redisClient.smembers("videocategory"+categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }
}
