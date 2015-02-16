package com.springapp.mvc;

import com.darfoo.backend.caches.CacheProtocol;
import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.TutorialDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
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
    VideoDao videoDao;
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CacheProtocol cacheProtocol;

    @Test
    public void insertVideoResourceIntoCache() {
        Video video = videoDao.getVideoByVideoId(35);
        cacheProtocol.insertResourceIntoCache(Video.class, video, "video");
    }

    @Test
    public void extractVideoResourceFromCache() {
        CacheSingleVideo result = (CacheSingleVideo) cacheProtocol.extractResourceFromCache(CacheSingleVideo.class, 35, "video");
        try {
            for (Field field : CacheSingleVideo.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertTutorialResourceIntoCache() {
        Tutorial tutorial = tutorialDao.getEducationVideoById(30);
        cacheProtocol.insertResourceIntoCache(Tutorial.class, tutorial, "tutorial");
    }

    @Test
    public void extractTutorialResourceFromCache() {
        CacheSingleVideo result = (CacheSingleVideo) cacheProtocol.extractResourceFromCache(CacheSingleVideo.class, 30, "tutorial");
        try {
            for (Field field : CacheSingleVideo.class.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + " -> " + field.get(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertMusicResourceIntoCache() {
        Author author = authorDao.getAuthor(15);
        cacheProtocol.insertResourceIntoCache(Author.class, author, "author");
    }
}
