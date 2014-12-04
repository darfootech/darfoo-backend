package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
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

    @RequestMapping(value = "/admin/video/all", method = RequestMethod.GET)
    public String showAllVideo(ModelMap modelMap, HttpSession session){
        Set<Video> s_videos = new HashSet<Video>();
        s_videos = videoDao.getAllVideo();
        modelMap.addAttribute("allvideos", s_videos);
        return "allvideo";
    }

    @RequestMapping(value = "/admin/video/{id}", method = RequestMethod.GET)
    public String showSingleVideo(@PathVariable String id, ModelMap modelMap){
        System.out.println(Integer.parseInt(id));
        Video video = videoDao.getVideoByVideoId(Integer.parseInt(id));
        modelMap.addAttribute("video", video);
        return "singlevideo";
    }
}
