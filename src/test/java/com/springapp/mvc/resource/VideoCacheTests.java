package com.springapp.mvc.resource;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.service.responsemodel.VideoCates;
import com.darfoo.backend.utils.ServiceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class VideoCacheTests {
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;
    @Autowired
    CategoryDao categoryDao;

    VideoCates videoCates = new VideoCates();

    @Test
    public void testDeleteVideo() {
        String key = "video-1";
        redisClient.delete(key);
    }

    @Test
    public void insertVideo() {
        Video video = (Video) commonDao.getResourceById(Video.class, 1);
        System.out.println(videoCacheDao.insertSingleVideo(video));
    }

    @Test
    public void getSingleVideo() {
        Integer id = 1;
        SingleVideo video = videoCacheDao.getSingleVideo(id);
        System.out.println(video.getTitle());
    }

    @Test
    public void cacheIndexVideos() {
        List<Video> latestVideos = commonDao.getResourcesByNewest(Video.class, 7);
        for (Video video : latestVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videoindex", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getIndexVideos() {
        Set<String> latestVideos = redisClient.smembers("videoindex");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : latestVideos) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void getRecommendVideos() {
        Set<String> recommendVideos = redisClient.smembers("videorecommend");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : recommendVideos) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheCategory() {
        String categories = "0-0-0-0";
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

        List<Video> targetVideos = categoryDao.getResourcesByCategories(Video.class, ServiceUtils.convertList2Array(targetCategories));
        for (Video video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videocategory" + categories, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getCategory() {
        String categories = "0-0-0-0";
        Set<String> categoryVideoKeys = redisClient.smembers("videocategory" + categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheAndGetMusic() {
        int id = 1;
        Music targetMusic = ((Video) commonDao.getResourceById(Video.class, id)).getMusic();
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Music music = (Music) commonDao.getResourceById(Music.class, music_id);
            System.out.println(musicCacheDao.insertSingleMusic(music));
        } else {
            System.out.println("没有关联伴奏");
        }
    }
}
