package com.darfoo.backend.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/resources/video/create", method = RequestMethod.POST)
    public @ResponseBody String createVideo(HttpServletRequest request){
        String videoTitle = request.getParameter("videotitle");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle);
        return "cleantha";
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

    @RequestMapping(value = "/resources/author/new", method = RequestMethod.GET)
    public String uploadAuthor(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "author");
        modelMap.addAttribute("resource", "author");
        return "uploadauthor";
    }

    @RequestMapping(value = "/resources/image/new", method = RequestMethod.GET)
    public String uploadImage(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "image");
        modelMap.addAttribute("resource", "image");
        return "uploadimage";
    }
}
