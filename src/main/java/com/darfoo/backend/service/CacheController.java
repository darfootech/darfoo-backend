package com.darfoo.backend.service;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
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

import java.util.ArrayList;
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
    CommonRedisClient redisClient;

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
        List<Video> recommendVideos = videoDao.getRecommendVideos(7);
        for (Video video : recommendVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("videorecommend", "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        Set<String> recommendVideoKeys = redisClient.smembers("videorecommend");
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : recommendVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }
        return result;
    }


    @RequestMapping(value = "/video/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> cacheIndexVideos() {
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
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
        for (String vkey : categoryVideoKeys){
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

        List<Education> targetVideos = educationDao.getEducationVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        for (Education video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("tutorialcategory" + categories, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        Set<String> categoryVideoKeys = redisClient.smembers("tutorialcategory" + categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys){
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
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
        List<Author> authors = authorDao.getAllAuthor();
        for (Author author : authors){
            int id = author.getId();
            long result = redisClient.sadd("authorindex", "author-" + id);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        Set<String> indexAuthorKeys = redisClient.smembers("authorindex");
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
    @ResponseBody public List<SingleVideo> getVideoListForAuthor(@PathVariable Integer id){
        List<SingleVideo> result = new ArrayList<SingleVideo>();
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
                SingleVideo video = videoCacheDao.getSingleVideo(vtid);
                result.add(video);
            }else if (vtflag.equals("tutorial")){
                SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(vtid);
                result.add(tutorial);
            }else {
                System.out.println("something is wrong");
            }
        }

        return result;
    }
}
