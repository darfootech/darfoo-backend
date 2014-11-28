package com.darfoo.backend.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    @RequestMapping(value = "/resources/video/new", method = RequestMethod.GET)
    public String uploadVideo(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "video");
        modelMap.addAttribute("resource", "video");
        return "uploadvideo";
    }

    @RequestMapping(value = "/resources/music/new", method = RequestMethod.GET)
    public String uploadMusic(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "music");
        modelMap.addAttribute("resource", "video");
        return "uploadmusic";
    }

    @RequestMapping(value = "/resources/tutorial/new", method = RequestMethod.GET)
    public String uploadTutorial(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "tutorial");
        modelMap.addAttribute("resource", "tutorial");
        return "uploadtutorial";
    }
}
