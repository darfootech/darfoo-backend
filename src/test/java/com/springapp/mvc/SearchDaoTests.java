package com.springapp.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
import com.darfoo.backend.service.responsemodel.CacheSingleVideo;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class SearchDaoTests {
    @Autowired
    private SearchDao searchDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    AuthorCacheDao authorCacheDao;
    @Autowired
    CommonRedisClient redisClient;

    @Test
    public void searchVideo() {
        String searchContent = "七里香";
        List<Video> l_video = searchDao.getVideoBySearch(searchContent);
        for (Video v : l_video) {
            System.out.println(v.getTitle());
            System.out.println("————————————————————————————————");
        }
    }

    @Test
    public void searchMusic() {
        String searchContent = "dear";
        List<Music> l = searchDao.getMusicBySearch(searchContent);
        for (Music v : l) {
            System.out.println(v.toString());
            System.out.println("————————————————————————————————");
        }
    }

    @Test
    public void searchEducation() {
        String searchContent = "heart";
        List<Education> l = searchDao.getEducationBySearch(searchContent);
        for (Education v : l) {
            System.out.println(v.toString());
            System.out.println("————————————————————————————————");
        }
    }

    @Test
    public void searchAuthor() {
        String searchContent = "周";
        List<Author> l = searchDao.getAuthorBySearch(searchContent);
        for (Author v : l) {
            System.out.println(v.toString());
            System.out.println("————————————————————————————————");
        }
    }

    @Test
    public void cacheSearchVideo() {
        String searchContent = "么么";
        System.out.println(searchContent);
        List<Video> videos = searchDao.getVideoBySearch(searchContent);
        for (Video video : videos) {
            int vid = video.getId();
            long status = redisClient.sadd("videosearch" + searchContent, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchVideoKeys = redisClient.smembers("videosearch" + searchContent);
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String key : searchVideoKeys) {
            System.out.println("key -> " + key);
            int vid = Integer.parseInt(key.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheSearchTutorial() {
        String searchContent = "呵呵";
        System.out.println(searchContent);
        List<Education> videos = searchDao.getEducationBySearch(searchContent);
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (Education video : videos) {
            int vid = video.getId();
            long status = redisClient.sadd("tutorialsearch" + searchContent, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchTutorialKeys = redisClient.smembers("tutorialsearch" + searchContent);
        for (String key : searchTutorialKeys) {
            System.out.println("key -> " + key);
            int tid = Integer.parseInt(key.split("-")[1]);
            CacheSingleVideo tutorial = tutorialCacheDao.getSingleTutorial(tid);
            System.out.println("title -> " + tutorial.getTitle());
            result.add(tutorial);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheSearchMusic() {
        String searchContent = "呵呵";
        System.out.println(searchContent);
        List<Music> musics = searchDao.getMusicBySearch(searchContent);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.sadd("musicsearch" + searchContent, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchMusicKeys = redisClient.smembers("musicsearch" + searchContent);
        for (String key : searchMusicKeys) {
            System.out.println("key -> " + key);
            int mid = Integer.parseInt(key.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        System.out.println(result.size());
    }

    @Test
    public void cacheSearchAuthor() {
        String searchContent = "周";
        System.out.println(searchContent);
        List<Author> authors = searchDao.getAuthorBySearch(searchContent);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (Author author : authors) {
            int aid = author.getId();
            long status = redisClient.sadd("authorsearch" + searchContent, "author-" + aid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchAuthorKeys = redisClient.smembers("authorsearch" + searchContent);
        for (String key : searchAuthorKeys) {
            System.out.println("key -> " + key);
            int aid = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(aid);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }

        System.out.println(result.size());
    }
}

