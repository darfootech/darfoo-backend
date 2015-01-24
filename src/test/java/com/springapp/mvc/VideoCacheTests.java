package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
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
    VideoDao videoDao;
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    CommonRedisClient redisClient;

    VideoCates videoCates = new VideoCates();

    @Test
    public void testAddVideo() {
        Video video = videoDao.getVideoByVideoId(1);
        boolean result = videoCacheDao.add(video);
        System.out.println(result);
    }

    @Test
    public void testDeleteVideo() {
        String key = "video-1";
        redisClient.delete(key);
    }

    @Test
    public void insertVideo() {
        Video video = videoDao.getVideoByVideoId(1);
        System.out.println(videoCacheDao.insertSingleVideo(video));
    }

    @Test
    public void getSingleVideo() {
        Integer id = 1;
        CacheSingleVideo video = videoCacheDao.getSingleVideo(id);
        System.out.println(video.getTitle());
    }

    @Test
    public void getSingleVideoFromPool() {
        Integer id = 1;
        SingleVideo video = videoCacheDao.getSingleVideoFromPool(id);
        System.out.println(video.getTitle());
    }

    @Test
    public void cacheIndexVideos() {
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
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
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : latestVideos) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheRecommendVideos() {
        List<Video> recommendVideos = videoDao.getRecommendVideos(7);
        for (Video video : recommendVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videorecommend", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getRecommendVideos() {
        Set<String> recommendVideos = redisClient.smembers("videorecommend");
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : recommendVideos) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
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

        List<Video> targetVideos = videoDao.getVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
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
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheAndGetMusic() {
        int id = 1;
        Music targetMusic = videoDao.getMusic(id);
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Music music = musicDao.getMusicByMusicId(music_id);
            System.out.println(musicCacheDao.insertSingleMusic(music));
        } else {
            System.out.println("没有关联伴奏");
        }
    }
}
