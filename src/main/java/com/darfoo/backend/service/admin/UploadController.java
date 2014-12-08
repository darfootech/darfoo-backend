package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
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
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    EducationDao educationDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    DanceDao danceDao;
    @Autowired
    DanceGroupImageDao danceGroupImageDao;

    public HashMap<String, Integer> insertSingleVideo(String videotitle, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle, String videoletter){
        System.out.println(authorname);

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Author targetAuthor = authorDao.getAuthor(authorname);
        if(targetAuthor != null){
            System.out.println(targetAuthor.getName());
        }
        else{
            System.out.println("无该author记录");
            resultMap.put("statuscode", 501);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoletter);
        if (isSingleLetter){
            System.out.println("是单个大写字母");
        }else{
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        int authorid = targetAuthor.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况
        Video queryVideo = videoDao.getVideoByTitleAuthorId(videotitle, authorid);
        if (queryVideo == null){
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
            return resultMap;
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
        int insertStatus = videoDao.insertSingleVideo(video);
        if (insertStatus == -1){
            System.out.println("插入视频失败");
        }else{
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        videoDao.updateVideoKeyById(insertStatus, videotitle + "-" + insertStatus);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        return resultMap;
    }

    public HashMap<String, Integer> insertSingleEducationVideo(String videotitle, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle){
        System.out.println(authorname);

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Author targetAuthor = authorDao.getAuthor(authorname);
        if(targetAuthor != null){
            System.out.println(targetAuthor.getName());
        }
        else{
            System.out.println("无该author记录");
            resultMap.put("statuscode", 501);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        int authorid = targetAuthor.getId();
        Education queryVideo = educationDao.getEducationByTitleAuthorId(videotitle, authorid);
        if (queryVideo == null){
            System.out.println("教程和作者id组合不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("教程和作者id组合已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Education video = new Education();
        video.setAuthor(targetAuthor);
        Image img = new Image();
        img.setImage_key(imagekey);
        video.setImage(img);
        EducationCategory speed = new EducationCategory();
        EducationCategory difficult = new EducationCategory();
        EducationCategory style = new EducationCategory();
        speed.setTitle(videospeed);
        difficult.setTitle(videodifficult);
        style.setTitle(videostyle);
        Set<EducationCategory> s_eCategory = video.getCategories();
        s_eCategory.add(speed);
        s_eCategory.add(difficult);
        s_eCategory.add(style);
        video.setTitle(videotitle);
        video.setVideo_key(videotitle);
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = educationDao.insertSingleEducationVideo(video);
        if (insertStatus == -1){
            System.out.println("插入教程失败");
        }else{
            System.out.println("插入教程成功，教程id是" + insertStatus);
        }

        educationDao.updateVideoKeyById(insertStatus, videotitle + "-" + insertStatus);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);

        return resultMap;
    }

    public HashMap<String, Integer> insertSingleMusic(String musictitle, String authorname, String imagekey, String musicbeat, String musicstyle, String musicletter){
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(musicletter);
        if (isSingleLetter){
            System.out.println("是单个大写字母");
        }else{
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Music queryMusic = musicDao.getMusicByMusicTitle(musictitle);
        if (queryMusic == null){
            System.out.println("伴奏不存在，可以进行插入");
        }else{
            System.out.println(queryMusic.toString(true));
            System.out.println("伴奏已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Music music = new Music();
        music.setAuthor(authorDao.getAllAuthor().get(0));
        music.setImage(image);
        MusicCategory beat = new MusicCategory();
        MusicCategory style = new MusicCategory();
        MusicCategory letter = new MusicCategory();
        beat.setTitle(musicbeat);
        style.setTitle(musicstyle);
        letter.setTitle(musicletter);
        Set<MusicCategory> s_mCategory = music.getCategories();
        s_mCategory.add(beat);
        s_mCategory.add(style);
        s_mCategory.add(letter);
        music.setTitle(musictitle);
        music.setMusic_key(musictitle);
        music.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = musicDao.insertSingleMusic(music);
        if (insertStatus == -1){
            System.out.println("插入伴奏失败");
        }else{
            System.out.println("插入伴奏成功，伴奏id是" + insertStatus);
        }

        musicDao.updateMusicKeyById(insertStatus, musictitle + "-" + insertStatus);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        return resultMap;
    }

    public int insertSingleAuthor(String authorname, String description){
        if(authorDao.isExistAuthor(authorname)){
            System.out.println("作者已存在");
            return 501;
        }else{
            System.out.println("无该author记录，可以创建");
        }

        Author author = new Author();
        author.setName(authorname);
        author.setDescription(description);
        authorDao.insertAuthor(author);

        return 200;
    }

    public int insertSingleDanceGroup(String groupname, String description, String imagekey){
        boolean isGroupExists = danceDao.isDanceGroupExists(groupname);
        if (isGroupExists){
            System.out.println("舞队已存在");
            return 501;
        }else{
            System.out.println("舞队不存在，可以新建舞队");
        }

        DanceGroupImage image = danceGroupImageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new DanceGroupImage();
            image.setImage_key(imagekey);
            danceGroupImageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return 503;
        }

        DanceGroup group = new DanceGroup();
        group.setName(groupname);
        group.setDescription(description);
        group.setUpdate_timestamp(System.currentTimeMillis());
        group.setImage(image);
        danceDao.insertSingleDanceGroup(group);

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
    public @ResponseBody String createVideo(HttpServletRequest request, HttpSession session){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter + " " + update_timestamp);

        HashMap<String, Integer> resultMap = this.insertSingleVideo(videoTitle, authorName, imagekey, videoSpeed, videoDifficult, videoStyle, videoLetter);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200){
            return statusCode+"";
        }else{
            int insertid = resultMap.get("insertid");
            session.setAttribute("videoKey", videoTitle + "-" + insertid + ".mp4");
            session.setAttribute("videoImage", imagekey);
            return statusCode+"";
        }
    }

    @RequestMapping(value = "/resources/videoresource/new", method = RequestMethod.GET)
    public String uploadVideoResource(){
        return "uploadvideoresource";
    }

    @RequestMapping("/resources/videoresource/create")
    public String createVideoResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String videoTitle = (String)session.getAttribute("videoKey");
        String imageKey = (String)session.getAttribute("videoImage");

        //String videoResourceName = videoresource.getOriginalFilename();
        //String imageResourceName = imageresource.getOriginalFilename();
        //System.out.println(videoResourceName + " " + imageResourceName);

        System.out.println(videoTitle + " " + imageKey);

        String videoStatusCode = "";
        String imageStatusCode = "";

        try {
            videoStatusCode = ServiceUtils.uploadLargeResource(videoresource, videoTitle);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imageKey);
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
    public @ResponseBody String createMusic(HttpServletRequest request, HttpSession session){
        String musicTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        String musicLetter = request.getParameter("musicletter").toUpperCase();
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imagekey + " " + musicBeat + " " + musicStyle + " " + musicLetter + " " + update_timestamp);

        HashMap<String, Integer> resultMap = this.insertSingleMusic(musicTitle, authorName, imagekey, musicBeat, musicStyle, musicLetter);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200){
            return statusCode+"";
        }else{
            int insertid = resultMap.get("insertid");
            session.setAttribute("musicTitle", musicTitle + "-" + insertid + ".mp3");
            session.setAttribute("musicImage", imagekey);
            return statusCode+"";
        }
    }

    @RequestMapping(value = "/resources/musicresource/new", method = RequestMethod.GET)
    public String uploadMusicResource(){
        return "uploadmusicresource";
    }

    @RequestMapping("/resources/musicresource/create")
    public String createMusicResource(@RequestParam("musicresource") CommonsMultipartFile musicresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String musicTitle = (String)session.getAttribute("musicTitle");
        String imageKey = (String)session.getAttribute("musicImage");

        String videoResourceName = musicresource.getOriginalFilename();
        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(videoResourceName + " " + imageResourceName);

        String musicStatusCode = "";
        String imageStatusCode = "";

        try {
            musicStatusCode = ServiceUtils.uploadLargeResource(musicresource, musicTitle);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imageKey);
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
    public @ResponseBody String createTutorial(HttpServletRequest request, HttpSession session){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);

        HashMap<String, Integer> resultMap = this.insertSingleEducationVideo(videoTitle, authorName, imagekey, videoSpeed, videoDifficult, videoStyle);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200){
            return statusCode+"";
        }else{
            int insertid = resultMap.get("insertid");
            session.setAttribute("tutorialKey", videoTitle + "-" + insertid + ".mp4");
            session.setAttribute("tutorialImage", imagekey);
            return statusCode+"";
        }
    }

    @RequestMapping(value = "/resources/tutorialresource/new", method = RequestMethod.GET)
    public String uploadTutorialResource(){
        return "uploadtutorialresource";
    }

    @RequestMapping("/resources/tutorialresource/create")
    public String createTutorialResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String tutorialTitle = (String)session.getAttribute("tutorialKey");
        String imagekey = (String)session.getAttribute("tutorialImage");

        //String videoResourceName = videoresource.getOriginalFilename();
        //String imageResourceName = imageresource.getOriginalFilename();
        //System.out.println(videoResourceName + " " + imageResourceName);

        System.out.println(tutorialTitle + " " + imagekey);

        String videoStatusCode = "";
        String imageStatusCode = "";

        try {
            videoStatusCode = ServiceUtils.uploadLargeResource(videoresource, tutorialTitle);
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
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

        int statusCode = this.insertSingleAuthor(name, description);
        return statusCode+"";
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
    public @ResponseBody String createTeam(HttpServletRequest request, HttpSession session){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + name + " " + description + " " + imagekey + " " + update_timestamp);

        session.setAttribute("dancegroupImage", imagekey);

        int statusCode = this.insertSingleDanceGroup(name, description, imagekey);
        return statusCode+"";
    }

    @RequestMapping(value = "/resources/teamresource/new", method = RequestMethod.GET)
    public String uploadTeamResource(){
        return "uploadteamresource";
    }

    @RequestMapping("/resources/teamresource/create")
    public String createTeamResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String imagekey = (String)session.getAttribute("dancegroupImage");

        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(imageResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
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
