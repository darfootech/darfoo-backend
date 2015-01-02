package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.IndexVideo;
import com.darfoo.backend.service.responsemodel.SingleVideo;
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
    CommonRedisClient redisClient;

    @Test
    public void testAddVideo(){
        Video video = videoDao.getVideoByVideoId(1);
        boolean result = videoCacheDao.add(video);
        System.out.println(result);
    }

    @Test
    public void testDeleteVideo(){
        String key = "video-1";
        redisClient.delete(key);
    }

    @Test
    public void insertVideo(){
        Video video = videoDao.getVideoByVideoId(1);
        System.out.println(videoCacheDao.insertSingleVideo(video));
    }

    @Test
    public void getSingleVideo(){
        Integer id = 1;
        SingleVideo video = videoCacheDao.getSingleVideo(id);
        System.out.println(video.getTitle());
    }

    @Test
    public void getSingleVideoFromPool(){
        Integer id = 1;
        SingleVideo video = videoCacheDao.getSingleVideoFromPool(id);
        System.out.println(video.getTitle());
    }

    @Test
    public void cacheIndexVideos(){
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
        for (Video video : latestVideos){
            int vid = video.getId();
            long result = redisClient.sadd("videoindex", "video-"+vid);
            Video indexVideo = videoDao.getVideoByVideoId(vid);
            videoCacheDao.insertSingleVideo(indexVideo);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getIndexVideos(){
        Set<String> latestVideos = redisClient.smembers("videoindex");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : latestVideos){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheRecommendVideos(){
        List<Video> recommendVideos = videoDao.getRecommendVideos(7);
        for (Video video : recommendVideos){
            int vid = video.getId();
            long result = redisClient.sadd("videorecommend", "video-"+vid);
            Video indexVideo = videoDao.getVideoByVideoId(vid);
            videoCacheDao.insertSingleVideo(indexVideo);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getRecommendVideos(){
        Set<String> recommendVideos = redisClient.smembers("videorecommend");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : recommendVideos){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }
}
