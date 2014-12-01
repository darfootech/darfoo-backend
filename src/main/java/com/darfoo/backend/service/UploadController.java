package com.darfoo.backend.service;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Set;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    VideoDao videoDao;

    public int insertSingleVideo(String videotitle, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle, String videoletter){
        System.out.println(authorname);
        Author targetAuthor = authorDao.getAuthor(authorname);
        if(targetAuthor != null){
            System.out.println(targetAuthor.getName());
        }
        else{
            System.out.println("无该author记录");
            return 501;
        }

        Video queryVideo = videoDao.getVideoByVideoTitle(videotitle);
        if (queryVideo == null){
            System.out.println("视频不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.toString(true));
            System.out.println("视频已存在，不可以进行插入了，是否需要修改");
            return 503;
        }

        Video video = new Video();
        video.setAuthor(targetAuthor);
        Image img = new Image();
        img.setImage_key(imagekey);
        video.setImage(img);
        VideoCategory speed = new VideoCategory();
        VideoCategory difficult = new VideoCategory();
        VideoCategory style = new VideoCategory();
        VideoCategory letter = new VideoCategory();
        speed.setTitle(videospeed);
        difficult.setTitle(videodifficult);
        style.setTitle(videostyle);
        letter.setTitle(videoletter);
        Set<VideoCategory> s_vCategory = video.getCategories();
        s_vCategory.add(speed);
        s_vCategory.add(difficult);
        s_vCategory.add(style);
        s_vCategory.add(letter);
        video.setTitle(videotitle);
        video.setVideo_key(videotitle);
        video.setUpdate_timestamp(System.currentTimeMillis());
        videoDao.inserSingleVideo(video);

        return 200;
    }

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
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter + " " + update_timestamp);

        int statusCode = this.insertSingleVideo(videoTitle, authorName, imagekey, videoSpeed, videoDifficult, videoStyle, videoLetter);
        System.out.println("status code is: " + statusCode);
        return statusCode+"";
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
        String musicLetter = request.getParameter("musicletter").toUpperCase();
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imagekey + " " + musicBeat + " " + musicStyle + " " + musicLetter + " " + update_timestamp);
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
