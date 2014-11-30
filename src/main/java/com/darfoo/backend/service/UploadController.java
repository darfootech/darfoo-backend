package com.darfoo.backend.service;

import com.darfoo.backend.utils.FileUtils;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {

    /*video part*/
    @RequestMapping(value = "/resources/video/new", method = RequestMethod.GET)
    public String uploadVideo(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "video");
        modelMap.addAttribute("resource", "video");
        return "uploadvideo";
    }

    @RequestMapping(value = "/resources/video/create", method = RequestMethod.POST)
    public @ResponseBody String createVideo(HttpServletRequest request){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/videoresource/new", method = RequestMethod.GET)
    public String uploadVideoResource(){
        return "uploadvideoresource";
    }

    @RequestMapping("/resources/videoresource/create")
    public String createVideoResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource){
        //upload
        String videoResourceName = videoresource.getOriginalFilename();
        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(videoResourceName + " " + imageResourceName);

        String videoStatusCode = "";
        String imageStatusCode = "";

        try {
            videoStatusCode = ServiceUtils.uploadLargeResource(videoresource);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (videoStatusCode.equals("200") && imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
    /*end of video part*/

    /*music part*/
    @RequestMapping(value = "/resources/music/new", method = RequestMethod.GET)
    public String uploadMusic(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "music");
        modelMap.addAttribute("resource", "music");
        return "uploadmusic";
    }

    @RequestMapping(value = "/resources/music/create", method = RequestMethod.POST)
    public @ResponseBody String createMusic(HttpServletRequest request){
        String musicTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imagekey + " " + musicBeat + " " + musicStyle + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/musicresource/new", method = RequestMethod.GET)
    public String uploadMusicResource(){
        return "uploadmusicresource";
    }

    @RequestMapping("/resources/musicresource/create")
    public String createMusicResource(@RequestParam("musicresource") CommonsMultipartFile musicresource, @RequestParam("imageresource") CommonsMultipartFile imageresource){
        //upload
        String videoResourceName = musicresource.getOriginalFilename();
        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(videoResourceName + " " + imageResourceName);

        String musicStatusCode = "";
        String imageStatusCode = "";

        try {
            musicStatusCode = ServiceUtils.uploadLargeResource(musicresource);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (musicStatusCode.equals("200") && imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
    /*end of music part*/

    /*tutorial part*/
    @RequestMapping(value = "/resources/tutorial/new", method = RequestMethod.GET)
    public String uploadTutorial(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "tutorial");
        modelMap.addAttribute("resource", "tutorial");
        return "uploadtutorial";
    }

    @RequestMapping(value = "/resources/tutorial/create", method = RequestMethod.POST)
    public @ResponseBody String createTutorial(HttpServletRequest request){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/tutorialresource/new", method = RequestMethod.GET)
    public String uploadTutorialResource(){
        return "uploadtutorialresource";
    }

    @RequestMapping("/resources/tutorialresource/create")
    public String createTutorialResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource){
        //upload
        String videoResourceName = videoresource.getOriginalFilename();
        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(videoResourceName + " " + imageResourceName);

        String videoStatusCode = "";
        String imageStatusCode = "";

        try {
            videoStatusCode = ServiceUtils.uploadLargeResource(videoresource);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (videoStatusCode.equals("200") && imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
    /*end of tutorial part*/

    /*author part*/
    //作者信息，不管是视频作者还是伴奏作者
    @RequestMapping(value = "/resources/author/new", method = RequestMethod.GET)
    public String uploadAuthor(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "author");
        modelMap.addAttribute("resource", "author");
        return "uploadauthor";
    }

    @RequestMapping(value = "/resources/author/create", method = RequestMethod.POST)
    public @ResponseBody String createAuthor(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        System.out.println("requests: " + name + " " + description);
        return "cleantha";
    }
    /*end of author part*/

    /*team part*/
    //新建舞队
    @RequestMapping(value = "/resources/team/new", method = RequestMethod.GET)
    public String uploadTeam(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "team");
        modelMap.addAttribute("resource", "team");
        return "uploadteam";
    }

    @RequestMapping(value = "/resources/team/create", method = RequestMethod.POST)
    public @ResponseBody String createTeam(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + name + " " + description + " " + imagekey + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/teamresource/new", method = RequestMethod.GET)
    public String uploadTeamResource(){
        return "uploadteamresource";
    }

    @RequestMapping("/resources/teamresource/create")
    public String createTeamResource(@RequestParam("imageresource") CommonsMultipartFile imageresource){
        //upload
        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(imageResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
    /*end of team part*/
}
