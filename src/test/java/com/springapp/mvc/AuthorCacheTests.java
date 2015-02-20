package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.CommonDao;
import com.darfoo.backend.dao.TutorialDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 15-1-3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class AuthorCacheTests {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    AuthorCacheDao authorCacheDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    TutorialDao educationDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;

    @Test
    public void cacheSingleAuthor() {
        Author author = (Author) commonDao.getResourceById(Author.class, 1);
        System.out.println(authorCacheDao.insertSingleAuthor(author));
    }

    @Test
    public void getSingleAuthor() {
        Integer id = 1;
        SingleAuthor author = authorCacheDao.getSingleAuthor(id);
        System.out.println(author.getName());
    }

    @Test
    public void cacheIndexAuthors() {
        List<Author> authors = commonDao.getAllResource(Author.class);
        for (Author author : authors) {
            int id = author.getId();
            long result = redisClient.sadd("authorindex", "author-" + id);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        Set<String> indexAuthorKeys = redisClient.smembers("authorindex");
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys) {
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheAuthorVideos() {
        int id = 11;
        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);

        for (Video video : videos) {
            int vid = video.getId();
            long status = redisClient.sadd("authorvideos" + id, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            long status = redisClient.sadd("authorvideos" + id, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        Set<String> authorVideoKeys = redisClient.smembers("authorvideos" + id);
        for (String key : authorVideoKeys) {
            System.out.println("key -> " + key);
            int vtid = Integer.parseInt(key.split("-")[1]);
            String vtflag = key.split("-")[0];
            if (vtflag.equals("video")) {
                SingleVideo video = videoCacheDao.getSingleVideo(vtid);
                result.add(video);
            } else if (vtflag.equals("tutorial")) {
                SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(vtid);
                result.add(tutorial);
            } else {
                System.out.println("something is wrong");
            }
        }

        System.out.println(result.size());
    }
}
