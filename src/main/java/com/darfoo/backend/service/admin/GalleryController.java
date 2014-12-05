package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.EducationCategory;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-4.
 * 用于展示已经上传的伴奏，视频和教学视频，全部或者是单个
 */

@Controller
public class GalleryController {
    @Autowired
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;

    @RequestMapping(value = "/admin/video/all", method = RequestMethod.GET)
    public String showAllVideo(ModelMap modelMap, HttpSession session){
        List<Video> s_videos = new ArrayList<Video>();
        s_videos = videoDao.getAllVideo();
        modelMap.addAttribute("allvideos", s_videos);
        return "allvideo";
    }

    @RequestMapping(value = "/admin/video/{id}", method = RequestMethod.GET)
    public String showSingleVideo(@PathVariable String id, ModelMap modelMap){
        System.out.println(Integer.parseInt(id));
        Video video = videoDao.getVideoByVideoId(Integer.parseInt(id));
        Set<VideoCategory> categories = video.getCategories();
        for (VideoCategory category : categories){
            int categoryid = category.getId();
            String categorytitle = category.getTitle();
            System.out.println(categoryid);
            System.out.println(categorytitle);
            if (categoryid >= 1 && categoryid <= 3){
                modelMap.addAttribute("speed", category.getTitle());
            }else if(categoryid >= 4 && categoryid <= 6){
                modelMap.addAttribute("difficult", category.getTitle());
            }else if(categoryid >= 7 && categoryid <= 17){
                modelMap.addAttribute("style", category.getTitle());
            }else if(categoryid >= 18 && categoryid <= 43){
                modelMap.addAttribute("letter", category.getTitle());
            }else{
                System.out.println("something is wrong with the category");
            }
        }
        modelMap.addAttribute("video", video);
        return "singlevideo";
    }

    @RequestMapping(value = "/admin/tutorial/all", method = RequestMethod.GET)
    public String showAllTutorial(ModelMap modelMap, HttpSession session){
        List<Education> s_tutorial = new ArrayList<Education>();
        s_tutorial = educationDao.getAllEdutcaion();
        modelMap.addAttribute("alltutorials", s_tutorial);
        return "alltutorial";
    }

    @RequestMapping(value = "/admin/tutorial/{id}", method = RequestMethod.GET)
    public String showSingleTutorial(@PathVariable String id, ModelMap modelMap){
        System.out.println(Integer.parseInt(id));
        Education tutorial = educationDao.getEducationVideoById(Integer.parseInt(id));
        Set<EducationCategory> categories = tutorial.getCategories();
        for (EducationCategory category : categories){
            int categoryid = category.getId();
            String categorytitle = category.getTitle();
            System.out.println(categoryid);
            System.out.println(categorytitle);
            if (categoryid >= 1 && categoryid <= 3){
                modelMap.addAttribute("speed", category.getTitle());
            }else if(categoryid >= 4 && categoryid <= 6){
                modelMap.addAttribute("difficult", category.getTitle());
            }else if(categoryid >= 7 && categoryid <= 9){
                modelMap.addAttribute("style", category.getTitle());
            }else{
                System.out.println("something is wrong with the category");
            }
        }
        modelMap.addAttribute("tutorial", tutorial);
        return "singletutorial";
    }
}
