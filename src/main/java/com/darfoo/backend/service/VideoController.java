package com.darfoo.backend.service;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.SearchDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.*;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.xml.parsing.XhtmlEntities;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-11-16.
 */

@Controller
@RequestMapping("/resources/video")
public class VideoController {
    @Autowired
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    SearchDao searchDao;

    QiniuUtils qiniuUtils = new QiniuUtils();
    VideoCates videoCates = new VideoCates();
    TutorialCates tutorialCates = new TutorialCates();

    //http://localhost:8080/darfoobackend/rest/resources/video/3
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleVideo(@PathVariable Integer id) {
        Video targetVideo = videoDao.getVideoByVideoId(id);
        String video_url = targetVideo.getVideo_key();
        String image_url = targetVideo.getImage().getImage_key();
        String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
        String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
        String video_title = targetVideo.getTitle();
        String author_name = "";
        if (targetVideo.getAuthor() != null){
            author_name = targetVideo.getAuthor().getName();
        }
        long update_timestamp = targetVideo.getUpdate_timestamp();
        return new SingleVideo(id, video_title, author_name, video_download_url, image_download_url, update_timestamp);
    }

    @RequestMapping(value = "/tutorial/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleTutorialVideo(@PathVariable Integer id){
        Education tutorial = educationDao.getEducationVideoById(id);
        String video_url = tutorial.getVideo_key();
        String image_url = tutorial.getImage().getImage_key();
        String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
        String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
        String video_title = tutorial.getTitle();
        String author_name = "";
        if (tutorial.getAuthor() != null){
            author_name = tutorial.getAuthor().getName();
        }
        long update_timestamp = tutorial.getUpdate_timestamp();
        return new SingleVideo(id, video_title, author_name, video_download_url, image_download_url, update_timestamp);
    }

    @RequestMapping(value = "/getmusic/{id}", method = RequestMethod.GET)
    public @ResponseBody SingleMusic getMusicByVideoId(@PathVariable String id){
        int currentVideoid = Integer.parseInt(id);
        Music targetMusic = videoDao.getMusic(currentVideoid);
        if (targetMusic != null){
            int music_id = targetMusic.getId();
            String music_url = targetMusic.getMusic_key() + ".mp3";
            //String image_url = targetMusic.getImage().getImage_key();
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music_url);
            //String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String title = targetMusic.getTitle();
            String author_name = "";
            if (targetMusic.getAuthor() != null){
                author_name = targetMusic.getAuthor().getName();
            }
            //return new SingleMusic(music_id, music_download_url, image_download_url, author_name, title);
            long update_timestamp = targetMusic.getUpdate_timestamp();
            return new SingleMusic(music_id, title, author_name, music_download_url, update_timestamp);
        }else{
            return new SingleMusic(-1, "", "", "", 0L);
        }
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/search?search=s
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleVideo> searchVideo(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Video> videos = searchDao.getVideoBySearch(searchContent);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Video video : videos){
            int id = video.getId();
            String title = video.getTitle();
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String image_url = video.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(id, title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/tutorial/search?search=heart
    @RequestMapping(value = "/tutorial/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleVideo> searchTutorial(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Education> videos = searchDao.getEducationBySearch(searchContent);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Education video : videos){
            int id = video.getId();
            String title = video.getTitle();
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String image_url = video.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(id, title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/recommend")
    public
    @ResponseBody
    List<SingleVideo> getRecmmendVideos() {
        List<Video> recommendVideos = videoDao.getRecommendVideos(7);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Video video : recommendVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String video_title = video.getTitle();
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(video_id, video_title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/index")
    public
    @ResponseBody
    List<SingleVideo> getIndexVideos() {
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Video video : latestVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String video_title = video.getTitle();
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(video_id, video_title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/category/2-0-0-0
    //根据类别获取普通视频
    @RequestMapping(value = "/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getVideosByCategories(@PathVariable String categories) {
        //System.out.println("categories request is " + categories + " !!!!!!!!!!");
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
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Video video : targetVideos) {
            int video_id = video.getId();
            String image_url = "";
            if (video.getImage() != null){
                image_url = video.getImage().getImage_key();
            }else{
                image_url = "";
            }
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String video_title = video.getTitle();
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(video_id, video_title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/category/tutorial/0-0-0-0
    //根据类别获取教学视频
    @RequestMapping(value = "/category/tutorial/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getTutorialVideosByCategories(@PathVariable String categories) {
        //System.out.println("categories request is " + categories + " !!!!!!!!!!");
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
        /*if (!requestCategories[3].equals("0")) {
            String teacherCate = tutorialCates.getTeacherCategory().get(requestCategories[3]);
            targetCategories.add(teacherCate);
        }*/

        //System.out.println(targetCategories.toString());

        List<Education> targetVideos = educationDao.getEducationVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (Education video : targetVideos) {
            int video_id = video.getId();
            String image_url = "";
            if (video.getImage() != null){
                image_url = video.getImage().getImage_key();
            }else{
                image_url = "";
            }
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String video_title = video.getTitle();
            String video_url = video.getVideo_key();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video_url);
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(video_id, video_title, author_name, video_download_url, image_download_url, update_timestamp));
        }
        return result;
    }
}
