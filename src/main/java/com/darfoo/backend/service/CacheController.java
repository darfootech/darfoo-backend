package com.darfoo.backend.service;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Video;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    EducationDao educationDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorCacheDao authorCacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    SearchDao searchDao;
    @Autowired
    CommonRedisClient redisClient;

    VideoCates videoCates = new VideoCates();
    TutorialCates tutorialCates = new TutorialCates();
    MusicCates musicCates = new MusicCates();

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    CacheSingleVideo getSingleVideoFromCache(@PathVariable String id) {
        Integer vid = Integer.parseInt(id);
        return videoCacheDao.getSingleVideo(vid);
    }

    @RequestMapping(value = "/tutorial/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    CacheSingleVideo getSingleTutorialFromCache(@PathVariable String id) {
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
    List<CacheSingleVideo> cacheRecmmendVideos() {
        List<Integer> recommendVideoids = ServiceUtils.getRecommendList("video");
        List<Video> recommendVideos = new ArrayList<Video>();
        for (Integer id : recommendVideoids){
            recommendVideos.add(videoDao.getVideoByVideoId(id));
        }
        List<Integer> recommendTutorialids = ServiceUtils.getRecommendList("tutorial");
        List<Education> recommendTutorials = new ArrayList<Education>();
        for (Integer id : recommendTutorialids){
            recommendTutorials.add(educationDao.getEducationVideoById(id));
        }

        for (Video video : recommendVideos) {
            int vid = video.getId();
            long status = redisClient.sadd("recommend", "recommendvideo-" + vid);
            videoCacheDao.insertRecommendVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Education video : recommendTutorials) {
            int vid = video.getId();
            long status = redisClient.sadd("recommend", "recommendvideo-" + vid);
            tutorialCacheDao.insertRecommendTutorial(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> recommendVideoKeys = redisClient.smembers("recommend");
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : recommendVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getRecommendVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }
        return result;
    }

    @RequestMapping(value = "/video/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CacheSingleVideo> cacheIndexVideos() {
        List<Video> latestVideos = videoDao.getVideosByNewest(12);
        for (Video video : latestVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videoindex", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        Set<String> latestVideoKeys = redisClient.smembers("videoindex");
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : latestVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/video/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CacheSingleVideo> getVideosByCategories(@PathVariable String categories) {
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
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : categoryVideoKeys){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/tutorial/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CacheSingleVideo> getTutorialsByCategories(@PathVariable String categories) {
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

        List<Education> targetVideos = educationDao.getEducationVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        for (Education video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("tutorialcategory" + categories, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        Set<String> categoryVideoKeys = redisClient.smembers("tutorialcategory" + categories);
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (String vkey : categoryVideoKeys){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            CacheSingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/music/category/{categories}", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleMusic> getMusicByCategories(@PathVariable String categories){
        //System.out.println("category request is " + categories + " !!!!!!!!!");
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")){
            String beatCate = musicCates.getBeatCategory().get(requestCategories[0]);
            targetCategories.add(beatCate);
        }
        if (!requestCategories[1].equals("0")){
            String styleCate = musicCates.getStyleCategory().get(requestCategories[1]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[2].equals("0")){
            String letterCate = requestCategories[2];
            targetCategories.add(letterCate);
        }

        List<Music> musics = musicDao.getMusicsByCategories(ServiceUtils.convertList2Array(targetCategories));
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int mid = music.getId();
            long status = redisClient.sadd("musiccategory" + categories, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> categoryMusicKeys = redisClient.smembers("musiccategory" + categories);
        for (String vkey : categoryMusicKeys){
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }

    @RequestMapping("/music/hottest")
    public @ResponseBody
    List<SingleMusic> getHottestMusics(){
        List<Music> musics = musicDao.getMusicsByHottest(5);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int mid = music.getId();
            long status = redisClient.sadd("musichottest", "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> hottestMusicKeys = redisClient.smembers("musichottest");
        for (String vkey : hottestMusicKeys){
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }

    @RequestMapping(value = "/video/getmusic/{id}", method = RequestMethod.GET)
    public @ResponseBody SingleMusic getMusicByVideoId(@PathVariable Integer id){
        Music targetMusic = videoDao.getMusic(id);
        if (targetMusic != null){
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Music music = musicDao.getMusicByMusicId(music_id);
            System.out.println(musicCacheDao.insertSingleMusic(music));
            return musicCacheDao.getSingleMusic(music_id);
        }else{
            return new SingleMusic(-1, "", "", "", 0L);
        }
    }

    @RequestMapping(value = "/author/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleAuthor cacheSingleAuthor(@PathVariable Integer id) {
        Author author = authorDao.getAuthor(id);
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
        for (Object[] rows : authorIdAndCnt){
            int authorid = (Integer)rows[1];
            System.out.println(authorid + " -> " + ((BigInteger)rows[0]).intValue());
            Author author = authorDao.getAuthor(authorid);
            long result = redisClient.lpush("authorindex", "author-" + authorid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        List<String> indexAuthorKeys = redisClient.lrange("authorindex", 0L, -1L);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys){
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }
        return result;
    }

    @RequestMapping(value = "/author/videos/{id}", method = RequestMethod.GET)
    @ResponseBody public List<CacheSingleVideo> getVideoListForAuthor(@PathVariable Integer id){
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        List<Video> videos = videoDao.getVideosByAuthorId(id);
        List<Education> tutorials = educationDao.getTutorialsByAuthorId(id);

        for (Video video : videos){
            int vid = video.getId();
            long status = redisClient.sadd("authorvideos" + id, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Education tutorial : tutorials){
            int tid = tutorial.getId();
            long status = redisClient.sadd("authorvideos" + id, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        Set<String> authorVideoKeys = redisClient.smembers("authorvideos" + id);
        for (String key : authorVideoKeys){
            System.out.println("key -> " + key);
            int vtid = Integer.parseInt(key.split("-")[1]);
            String vtflag = key.split("-")[0];
            if (vtflag.equals("video")){
                CacheSingleVideo video = videoCacheDao.getSingleVideo(vtid);
                result.add(video);
            }else if (vtflag.equals("tutorial")){
                CacheSingleVideo tutorial = tutorialCacheDao.getSingleTutorial(vtid);
                result.add(tutorial);
            }else {
                System.out.println("something is wrong");
            }
        }

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
    public @ResponseBody
    List<CacheSingleVideo> searchVideo(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        Set<Integer> videoids = new HashSet<Integer>();
        Set<Integer> tutorialids = new HashSet<Integer>();
        List<Video> videos = searchDao.getVideoBySearch(searchContent);
        List<Education> tutorials = searchDao.getEducationBySearch(searchContent);
        for (Video video : videos){
            videoids.add(video.getId());
        }

        for (Education tutorial : tutorials){
            tutorialids.add(tutorial.getId());
        }

        List<Author> authors = searchDao.getAuthorBySearch(searchContent);
        for (Author author : authors){
            int aid = author.getId();
            List<Video> authorvideos = videoDao.getVideosByAuthorId(aid);
            List<Education> authortutorials = educationDao.getTutorialsByAuthorId(aid);
            for (Video video : authorvideos){
                videoids.add(video.getId());
            }

            for (Education tutorial : authortutorials){
                tutorialids.add(tutorial.getId());
            }
        }

        for (Integer vid : videoids){
            Video video = videoDao.getVideoByVideoId(vid);
            long status = redisClient.sadd("videosearch" + searchContent, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Integer tid : tutorialids){
            Education tutorial = educationDao.getEducationVideoById(tid);
            long status = redisClient.sadd("videosearch" + searchContent, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();

        Set<String> searchVideoKeys = redisClient.smembers("videosearch" + searchContent);
        for (String key : searchVideoKeys){
            System.out.println("key -> " + key);
            int vid = Integer.parseInt(key.split("-")[1]);
            String flag = key.split("-")[0];
            if (flag.equals("video")){
                CacheSingleVideo video = videoCacheDao.getSingleVideo(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
            if (flag.equals("tutorial")){
                CacheSingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
                System.out.println("title -> " + video.getTitle());
                result.add(video);
            }
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/tutorial/search?search=heart
    @RequestMapping(value = "/tutorial/search", method = RequestMethod.GET)
    public @ResponseBody
    List<CacheSingleVideo> searchTutorial(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Education> videos = searchDao.getEducationBySearch(searchContent);
        List<CacheSingleVideo> result = new ArrayList<CacheSingleVideo>();
        for (Education video : videos){
            int vid = video.getId();
            long status = redisClient.sadd("tutorialsearch" + searchContent, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchTutorialKeys = redisClient.smembers("tutorialsearch" + searchContent);
        for (String key : searchTutorialKeys){
            System.out.println("key -> " + key);
            int tid = Integer.parseInt(key.split("-")[1]);
            CacheSingleVideo tutorial = tutorialCacheDao.getSingleTutorial(tid);
            System.out.println("title -> " + tutorial.getTitle());
            result.add(tutorial);
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/search?search=s
    @RequestMapping(value = "/music/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleMusic> searchMusic(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Music> musics = searchDao.getMusicBySearch(searchContent);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int mid = music.getId();
            long status = redisClient.sadd("musicsearch" + searchContent, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchMusicKeys = redisClient.smembers("musicsearch" + searchContent);
        for (String key : searchMusicKeys){
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
    public @ResponseBody
    List<SingleAuthor> searchAuthor(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Author> authors = searchDao.getAuthorBySearch(searchContent);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (Author author : authors){
            int aid = author.getId();
            long status = redisClient.sadd("authorsearch" + searchContent, "author-" + aid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + status);
        }

        Set<String> searchAuthorKeys = redisClient.smembers("authorsearch" + searchContent);
        for (String key : searchAuthorKeys){
            System.out.println("key -> " + key);
            int aid = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(aid);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }

        return result;
    }
}
