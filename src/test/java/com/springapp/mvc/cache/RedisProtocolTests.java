package com.springapp.mvc.cache;

import com.darfoo.backend.caches.cota.CacheProtocol;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;

/**
 * Created by zjh on 15-2-14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class RedisProtocolTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheProtocol cacheProtocol;

    @Test
    public void insertVideoResourceIntoCache() {
        Video video = (Video) commonDao.getResourceById(Video.class, 35);
        cacheProtocol.insertResourceIntoCache(Video.class, video, "video");
    }

    @Test
    public void extractVideoResourceFromCache() {
        SingleVideo result = (SingleVideo) cacheProtocol.extractResourceFromCache(SingleVideo.class, 35, "video");
        try {
            for (Field field : SingleVideo.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertTutorialResourceIntoCache() {
        Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, 30);
        cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "tutorial");
    }

    @Test
    public void extractTutorialResourceFromCache() {
        SingleVideo result = (SingleVideo) cacheProtocol.extractResourceFromCache(SingleVideo.class, 30, "tutorial");
        try {
            for (Field field : SingleVideo.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertAuthorResourceIntoCache() {
        Author author = (Author) commonDao.getResourceById(Author.class, 13);
        cacheProtocol.insertResourceIntoCache(Author.class, author, "author");
    }

    @Test
    public void extractAuthorResourceFromCache() {
        SingleAuthor result = (SingleAuthor) cacheProtocol.extractResourceFromCache(SingleAuthor.class, 15, "author");
        try {
            for (Field field : SingleAuthor.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertMusicResourceIntoCache() {
        Music music = (Music) commonDao.getResourceById(Music.class, 30);
        cacheProtocol.insertResourceIntoCache(Music.class, music, "music");
    }

    @Test
    public void extractMusicResourceFromCache() {
        SingleMusic result = (SingleMusic) cacheProtocol.extractResourceFromCache(SingleMusic.class, 30, "music");
        try {
            for (Field field : SingleMusic.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}