package com.darfoo.backend.service;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.dao.resource.VideoDao;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

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
    TutorialDao educationDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorCacheDao authorCacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;

    VideoCates videoCates = new VideoCates();
    TutorialCates tutorialCates = new TutorialCates();
    MusicCates musicCates = new MusicCates();

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
        List<Video> recommendVideos = recommendDao.getRecommendResources(Video.class);
        List<Tutorial> recommendTutorials = recommendDao.getRecommendResources(Tutorial.class);

        for (Video video : recommendVideos) {
            int vid = video.getId();
            long status = redisClient.sadd("recommend", "recommendvideo-" + vid);
            videoCacheDao.insertRecommendVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Tutorial video : recommendTutorials) {
            int vid = video.getId();
            long status = redisClient.sadd("recommend", "recommendvideo-" + vid);
            tutorialCacheDao.insertRecommendTutorial(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> recommendVideoKeys = redisClient.smembers("recommend");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : recommendVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getRecommendVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }
        return result;
    }

    @RequestMapping(value = "/video/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> cacheIndexVideos() {
        List<Video> latestVideos = commonDao.getResourcesByNewest(Video.class, 12);
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
            long result = redisClient.sadd("videocategory" + categories, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }

        Set<String> categoryVideoKeys = redisClient.smembers("videocategory" + categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/video/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getVideosByCategoriesByPage(@PathVariable String categories, @PathVariable Integer page) {
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

        List<Video> targetVideos = videoDao.getVideosByCategoriesByPage(ServiceUtils.convertList2Array(targetCategories), page);
        System.out.println("target video size -> " + targetVideos.size());

        for (Video video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.lpush("videocategory" + categories + "page" + page, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }

        List<String> categoryVideoKeys = redisClient.lrange("videocategory" + categories + "page" + page, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/tutorial/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getTutorialsByCategories(@PathVariable String categories) {
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")) {
            String speedCate = tutorialCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")) {
            String difficultyCate = tutorialCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
        }
        if (!requestCategories[2].equals("0")) {
            String styleCate = tutorialCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }

        List<Tutorial> targetVideos = educationDao.getEducationVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        for (Tutorial video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("tutorialcategory" + categories, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        Set<String> categoryVideoKeys = redisClient.smembers("tutorialcategory" + categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/tutorial/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getTutorialsByCategoriesByPage(@PathVariable String categories, @PathVariable Integer page) {
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")) {
            String speedCate = tutorialCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")) {
            String difficultyCate = tutorialCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
        }
        if (!requestCategories[2].equals("0")) {
            String styleCate = tutorialCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }

        List<Tutorial> targetVideos = educationDao.getTutorialsByCategoriesByPage(ServiceUtils.convertList2Array(targetCategories), page);
        for (Tutorial video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.lpush("tutorialcategory" + categories + "page" + page, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        List<String> categoryVideoKeys = redisClient.lrange("tutorialcategory" + categories + "page" + page, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/music/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> getMusicByCategories(@PathVariable String categories) {
        //System.out.println("category request is " + categories + " !!!!!!!!!");
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

        List<Music> musics = musicDao.getMusicsByCategories(ServiceUtils.convertList2Array(targetCategories));
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

        return result;
    }

    @RequestMapping(value = "/music/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> getMusicByCategoriesByPage(@PathVariable String categories, @PathVariable Integer page) {
        //System.out.println("category request is " + categories + " !!!!!!!!!");
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

        List<Music> musics = musicDao.getMusicsByCategoriesByPage(ServiceUtils.convertList2Array(targetCategories), page);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.lpush("musiccategory" + categories + "page" + page, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        List<String> categoryMusicKeys = redisClient.lrange("musiccategory" + categories + "page" + page, 0L, -1L);
        for (String vkey : categoryMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }

    @RequestMapping("/music/hottest")
    public
    @ResponseBody
    List<SingleMusic> getHottestMusics() {
        List<Music> musics = musicDao.getMusicsByHottest(5);
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

        return result;
    }

    @RequestMapping(value = "/video/getmusic/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleMusic getMusicByVideoId(@PathVariable Integer id) {
        Music targetMusic = videoDao.getMusic(id);
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Music music = (Music) commonDao.getResourceById(Music.class, music_id);
            System.out.println(musicCacheDao.insertSingleMusic(music));
            return musicCacheDao.getSingleMusic(music_id);
        } else {
            return new SingleMusic(-1, "", "", "", 0L);
        }
    }

    @RequestMapping(value = "/author/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleAuthor cacheSingleAuthor(@PathVariable Integer id) {
        Author author = (Author) commonDao.getResourceById(Author.class, id);
        System.out.println(authorCacheDao.insertSingleAuthor(author));
        SingleAuthor result = authorCacheDao.getSingleAuthor(id);
        return result;
    }

    @RequestMapping(value = "/author/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleAuthor> cacheIndexAuthors() {
        /*List<Author> authors = authorDao.getAllAuthor();
        for (Author author : authors){
            int id = author.getId();
            long result = redisClient.sadd("authorindex", "author-" + id);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }*/

        List<Object[]> authorIdAndCnt = authorDao.getAuthorOrderByVideoCountDesc();
        for (Object[] rows : authorIdAndCnt) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, authorid);
            long result = redisClient.lpush("authorindex", "author-" + authorid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        List<String> indexAuthorKeys = redisClient.lrange("authorindex", 0L, -1L);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys) {
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }
        return result;
    }

    @RequestMapping(value = "/author/index/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleAuthor> cacheIndexAuthorsByPage(@PathVariable Integer page) {
        /*List<Author> authors = authorDao.getAllAuthor();
        for (Author author : authors){
            int id = author.getId();
            long result = redisClient.sadd("authorindex", "author-" + id);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }*/

        List<Object[]> authorIdAndCnt = authorDao.getAuthorOrderByVideoCountDescByPage(page);
        for (Object[] rows : authorIdAndCnt) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, authorid);
            long result = redisClient.lpush("authorindexpage" + page, "author-" + authorid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        List<String> indexAuthorKeys = redisClient.lrange("authorindexpage" + page, 0L, -1L);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys) {
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }
        return result;
    }

    @RequestMapping(value = "/author/videos/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<SingleVideo> getVideoListForAuthor(@PathVariable Integer id) {
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

        return result;
    }

    @RequestMapping(value = "/author/videos/{id}/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public List<SingleVideo> getVideoListForAuthorByPage(@PathVariable Integer id, @PathVariable Integer page) {
        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);

        int pageSize = 12;
        String rediskey = "authorvideos" + id + "page";

        for (Video video : videos) {
            int vid = video.getId();
            long status = redisClient.lpush(rediskey, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            long status = redisClient.lpush(rediskey, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        List<String> authorVideoKeys = redisClient.lrange(rediskey, start, end);
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

        System.out.println("videolist size -> " + result.size());

        return result;
    }

    /*search cache*/
    //http://localhost:8080/darfoobackend/rest/resources/video/search?search=s
    /*@RequestMapping(value = "/video/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleVideo> searchVideo(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Video> videos = searchDao.getVideoBySearch(searchContent);
        for (Video video : videos){
            int vid = video.getId();
            long status = redisClient.sadd("videosearch" + searchContent, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchVideoKeys = redisClient.smembers("videosearch" + searchContent);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String key : searchVideoKeys){
            System.out.println("key -> " + key);
            int vid = Integer.parseInt(key.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }
        return result;
    }*/

    @RequestMapping(value = "/video/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> searchVideo(HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        Set<Integer> videoids = new HashSet<Integer>();
        Set<Integer> tutorialids = new HashSet<Integer>();
        List<Video> videos = commonDao.getResourceBySearch(Video.class, searchContent);
        List<Tutorial> tutorials = commonDao.getResourceBySearch(Tutorial.class, searchContent);
        for (Video video : videos) {
            videoids.add(video.getId());
        }

        for (Tutorial tutorial : tutorials) {
            tutorialids.add(tutorial.getId());
        }

        List<Author> authors = commonDao.getResourceBySearch(Author.class, searchContent);
        for (Author author : authors) {
            int aid = author.getId();

            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List<Video> authorvideos = commonDao.getResourcesByFields(Video.class, conditions);
            List<Tutorial> authortutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);
            for (Video video : authorvideos) {
                videoids.add(video.getId());
            }

            for (Tutorial tutorial : authortutorials) {
                tutorialids.add(tutorial.getId());
            }
        }

        for (Integer vid : videoids) {
            Video video = (Video) commonDao.getResourceById(Video.class, vid);
            long status = redisClient.sadd("videosearch" + searchContent, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Integer tid : tutorialids) {
            Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, tid);
            long status = redisClient.sadd("videosearch" + searchContent, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        List<SingleVideo> result = new ArrayList<SingleVideo>();

        Set<String> searchVideoKeys = redisClient.smembers("videosearch" + searchContent);
        for (String key : searchVideoKeys) {
            System.out.println("key -> " + key);
            int vid = Integer.parseInt(key.split("-")[1]);
            String flag = key.split("-")[0];
            if (flag.equals("video")) {
                SingleVideo video = videoCacheDao.getSingleVideo(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
            if (flag.equals("tutorial")) {
                SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
        }

        return result;
    }

    @RequestMapping(value = "/video/search/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> searchVideoByPage(@PathVariable Integer page, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        Set<Integer> videoids = new HashSet<Integer>();
        Set<Integer> tutorialids = new HashSet<Integer>();
        List<Video> videos = commonDao.getResourceBySearch(Video.class, searchContent);
        List<Tutorial> tutorials = commonDao.getResourceBySearch(Tutorial.class, searchContent);
        for (Video video : videos) {
            videoids.add(video.getId());
        }

        for (Tutorial tutorial : tutorials) {
            tutorialids.add(tutorial.getId());
        }

        List<Author> authors = commonDao.getResourceBySearch(Author.class, searchContent);
        for (Author author : authors) {
            int aid = author.getId();

            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List<Video> authorvideos = commonDao.getResourcesByFields(Video.class, conditions);
            List<Tutorial> authortutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);
            for (Video video : authorvideos) {
                videoids.add(video.getId());
            }

            for (Tutorial tutorial : authortutorials) {
                tutorialids.add(tutorial.getId());
            }
        }

        int pageSize = 12;
        String rediskey = "videosearch" + searchContent + "page";

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        for (Integer vid : videoids) {
            Video video = (Video) commonDao.getResourceById(Video.class, vid);
            long status = redisClient.lpush(rediskey, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Integer tid : tutorialids) {
            Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, tid);
            long status = redisClient.lpush(rediskey, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        List<SingleVideo> result = new ArrayList<SingleVideo>();

        List<String> searchVideoKeys = redisClient.lrange(rediskey, start, end);
        for (String key : searchVideoKeys) {
            System.out.println("key -> " + key);
            int vid = Integer.parseInt(key.split("-")[1]);
            String flag = key.split("-")[0];
            if (flag.equals("video")) {
                SingleVideo video = videoCacheDao.getSingleVideo(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
            if (flag.equals("tutorial")) {
                SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/tutorial/search?search=heart
    @RequestMapping(value = "/tutorial/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> searchTutorial(HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Tutorial> videos = commonDao.getResourceBySearch(Tutorial.class, searchContent);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Tutorial video : videos) {
            int vid = video.getId();
            long status = redisClient.sadd("tutorialsearch" + searchContent, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchTutorialKeys = redisClient.smembers("tutorialsearch" + searchContent);
        for (String key : searchTutorialKeys) {
            System.out.println("key -> " + key);
            int tid = Integer.parseInt(key.split("-")[1]);
            SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(tid);
            System.out.println("title -> " + tutorial.getTitle());
            result.add(tutorial);
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/search?search=s
    @RequestMapping(value = "/music/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> searchMusic(HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Music> musics = commonDao.getResourceBySearch(Music.class, searchContent);
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

        return result;
    }

    @RequestMapping(value = "/music/search/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> searchMusic(@PathVariable Integer page, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Music> musics = commonDao.getResourceBySearch(Music.class, searchContent);
        List<SingleMusic> result = new ArrayList<SingleMusic>();

        int pageSize = 22;
        String rediskey = "musicsearch" + searchContent + "page";

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.lpush(rediskey, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        List<String> searchMusicKeys = redisClient.lrange(rediskey, start, end);
        for (String key : searchMusicKeys) {
            System.out.println("key -> " + key);
            int mid = Integer.parseInt(key.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/author/search?search=heart
    @RequestMapping(value = "/author/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleAuthor> searchAuthor(HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Author> authors = commonDao.getResourceBySearch(Author.class, searchContent);
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

        return result;
    }

    @RequestMapping(value = "/video/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getSidebarVideos(@PathVariable Integer id) {
        List<Video> sidebarVideos = commonDao.getSideBarResources(Video.class, id);
        String rediskey = "videosidebar" + id;
        for (Video video : sidebarVideos) {
            int vid = video.getId();
            long result = redisClient.lpush(rediskey, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        List<String> sideBarVideoKeys = redisClient.lrange(rediskey, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : sideBarVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/tutorial/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getSidebarTutorials(@PathVariable Integer id) {
        List<Tutorial> sidebarTutorials = commonDao.getSideBarResources(Tutorial.class, id);
        String rediskey = "tutorialsidebar" + id;
        for (Tutorial video : sidebarTutorials) {
            int vid = video.getId();
            long result = redisClient.lpush(rediskey, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        List<String> sidebarTutorialKeys = redisClient.lrange(rediskey, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : sidebarTutorialKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/music/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> getSidebarMusics(@PathVariable Integer id) {
        List<Music> musics = commonDao.getSideBarResources(Music.class, id);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        String rediskey = "musicsidebar" + id;
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.lpush(rediskey, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        List<String> sidebarMusicKeys = redisClient.lrange(rediskey, 0L, -1L);
        for (String vkey : sidebarMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }
}
