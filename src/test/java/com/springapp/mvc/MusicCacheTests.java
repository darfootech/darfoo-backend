package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void testDeleteMusic(){
        String key = "music-1";
        redisClient.delete(key);
    }

    @Test
    public void insertMusic(){
        Music music = musicDao.getMusicByMusicId(1);
        System.out.println(musicCacheDao.insertSingleMusic(music));
    }

    @Test
    public void getSingleMusic(){
        Integer id = 1;
        SingleMusic music = musicCacheDao.getSingleMusic(id);
        System.out.println(music.getTitle());
    }

}
