package com.darfoo.backend.service;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.SearchDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    VideoCates videoCates = new VideoCates();
    TutorialCates tutorialCates = new TutorialCates();

    //http://localhost:8080/darfoobackend/rest/resources/video/3
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleVideo(@PathVariable String id) {
        Video targetVideo = videoDao.getVideoByVideoId(Integer.parseInt(id));
        int video_id = targetVideo.getId();
        String video_url = targetVideo.getVideo_key();
        String video_title = targetVideo.getTitle();
        return new SingleVideo(video_id, video_title, video_url);
    }

    @RequestMapping(value = "/tutorial/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo getSingleTutorialVideo(@PathVariable String id){
        Education tutorial = educationDao.getEducationVideoById(Integer.parseInt(id));
        int video_id = tutorial.getId();
        String video_url = tutorial.getVideo_key();
        String video_title = tutorial.getTitle();
        return new SingleVideo(video_id, video_title, video_url);
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/search/s
    @RequestMapping(value = "/search/{content}", method = RequestMethod.GET)
    public @ResponseBody
    List<SearchVideo> searchVideo(@PathVariable String content){
        List<Video> videos = searchDao.getVideoBySearch(content);
        List<SearchVideo> result = new ArrayList<SearchVideo>();
        for (Video video : videos){
            int id = video.getId();
            String title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SearchVideo(id, title, update_timestamp));
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/tutorial/search/heart
    @RequestMapping(value = "/tutorial/search/{content}", method = RequestMethod.GET)
    public @ResponseBody
    List<SearchVideo> searchTutorial(@PathVariable String content){
        List<Education> videos = searchDao.getEducationBySearch(content);
        List<SearchVideo> result = new ArrayList<SearchVideo>();
        for (Education video : videos){
            int id = video.getId();
            String title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new SearchVideo(id, title, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/recommend")
    public
    @ResponseBody
    List<IndexVideo> getRecmmendVideos() {
        List<Video> recommendVideos = videoDao.getRecommendVideos(3);
        List<IndexVideo> result = new ArrayList<IndexVideo>();
        for (Video video : recommendVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new IndexVideo(video_id, video_title, image_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/index")
    public
    @ResponseBody
    List<IndexVideo> getIndexVideos() {
        List<Video> latestVideos = videoDao.getLatestVideos(5);
        List<IndexVideo> result = new ArrayList<IndexVideo>();
        for (Video video : latestVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new IndexVideo(video_id, video_title, image_url, update_timestamp));
        }
        return result;
    }

    String[] convertList2Array(List<String> vidoes) {
        String[] stockArr = new String[vidoes.size()];
        stockArr = vidoes.toArray(stockArr);
        /*for(String s : stockArr)
            System.out.println(s);*/
        return stockArr;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/category/2-0-0-0
    //根据类别获取普通视频
    @RequestMapping(value = "/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CategoryVideo> getVideosByCategories(@PathVariable String categories) {
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

        List<Video> targetVideos = videoDao.getVideosByCategories(convertList2Array(targetCategories));
        List<CategoryVideo> result = new ArrayList<CategoryVideo>();
        for (Video video : targetVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            String author_name = video.getAuthor().getName();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new CategoryVideo(video_id, video_title, author_name, image_url, update_timestamp));
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/video/category/teach/0-0-0-0
    //根据类别获取教学视频
    @RequestMapping(value = "/category/tutorial/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CategoryVideo> getTutorialVideosByCategories(@PathVariable String categories) {
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

        List<Education> targetVideos = educationDao.getEducationVideosByCategories(convertList2Array(targetCategories));
        List<CategoryVideo> result = new ArrayList<CategoryVideo>();
        for (Education video : targetVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String author_name = video.getAuthor().getName();
            String video_title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new CategoryVideo(video_id, video_title, author_name, image_url, update_timestamp));
        }
        return result;
    }
}
