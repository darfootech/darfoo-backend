package com.springapp.mvc;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.service.responsemodel.MusicCates;
import com.darfoo.backend.service.responsemodel.SingleMusic;
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
 * Created by zjh on 14-12-18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class MusicCacheTests {
    @Autowired
    MusicDao musicDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;
    @Autowired
    CategoryDao categoryDao;

    MusicCates musicCates = new MusicCates();

    @Test
    public void testDeleteMusic() {
        String key = "music-1";
        redisClient.delete(key);
    }

    @Test
    public void insertMusic() {
        Music music = (Music) commonDao.getResourceById(Music.class, 1);
        System.out.println(musicCacheDao.insertSingleMusic(music));
    }

    @Test
    public void getSingleMusic() {
        Integer id = 1;
        SingleMusic music = musicCacheDao.getSingleMusic(id);
        System.out.println(music.getTitle());
    }

    @Test
    public void cacheCategory() {
        String categories = "1-0-0";
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")) {
            String beatCate = musicCates.getBeatCategory().get(requestCategories[0]);
            targetCategories.add(beatCate);
        }
        if (!requestCategories[1].equals("0")) {
            String styleCate = musicCates.getStyleCategory().get(requestCategories[1]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[2].equals("0")) {
            String letterCate = requestCategories[2];
            targetCategories.add(letterCate);
        }

        List<Music> musics = categoryDao.getResourcesByCategories(Music.class, ServiceUtils.convertList2Array(targetCategories));
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.sadd("musiccategory" + categories, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> categoryMusicKeys = redisClient.smembers("musiccategory" + categories);
        for (String vkey : categoryMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheHottestMusics() {
        List<Music> musics = commonDao.getResourcesByHottest(Music.class, 5);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.sadd("musichottest", "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> hottestMusicKeys = redisClient.smembers("musichottest");
        for (String vkey : hottestMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }
        System.out.println(result.size());
    }
}
