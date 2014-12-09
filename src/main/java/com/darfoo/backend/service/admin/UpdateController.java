package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjh on 14-12-4.
 */

@Controller
public class UpdateController {
    @Autowired
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    DanceDao danceDao;

    public int checkVideoTitleAuthorIdDuplicate(String videoTitle, String authorName){
        Author a = authorDao.getAuthor(authorName);
        if(a != null){
            System.out.println(a.getName());
        }
        else{
            System.out.println("无该author记录");
        }

        int authorid = a.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况
        Video queryVideo = videoDao.getVideoByTitleAuthorId(videoTitle, authorid);
        if (queryVideo == null){
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
            return 1;
        }else{
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    public int checkTutorialTitleAuthorIdDuplicate(String tutorialTitle, String authorName){
        Author a = authorDao.getAuthor(authorName);
        if(a != null){
            System.out.println(a.getName());
        }
        else{
            System.out.println("无该author记录");
        }

        int authorid = a.getId();
        Education queryVideo = educationDao.getEducationByTitleAuthorId(tutorialTitle, authorid);
        if (queryVideo == null){
            System.out.println("教程和作者id组合不存在，可以进行插入");
            return 1;
        }else{
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("教程和作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    public int checkMusicTitleAuthorIdDuplicate(String musicTitle, String authorName){
        Author a = authorDao.getAuthor(authorName);
        if(a != null){
            System.out.println(a.getName());
        }
        else{
            System.out.println("无该author记录");
        }

        int authorid = a.getId();
        Music queryMusic = musicDao.getMusicByTitleAuthorId(musicTitle, authorid);
        if (queryMusic == null){
            System.out.println("伴奏与作者id组合不存在，可以进行插入");
            return 1;
        }else{
            System.out.println(queryMusic.getId());
            System.out.println(queryMusic.getAuthor().getName());
            System.out.println("伴奏与作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    @RequestMapping(value = "/admin/video/update", method = RequestMethod.POST)
    public @ResponseBody String updateVideo(HttpServletRequest request, HttpSession session){
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imageKey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter);

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoLetter);
        if (isSingleLetter){
            System.out.println("是单个大写字母");
        }else{
            System.out.println("不是单个大写字母");
            return 505+"";
        }

        int duplicateCode = checkVideoTitleAuthorIdDuplicate(videoTitle, authorName);
        if (duplicateCode == 0){
            return 501+"";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = videoDao.updateVideoCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficult);
        categoryTitles.add(videoStyle);
        categoryTitles.add(videoLetter.toUpperCase());

        String newVideoKey = videoDao.getVideoByVideoId(vid).getVideo_key().split("\\.")[0] + "." + videoType;
        videoDao.updateVideoKeyById(vid, newVideoKey);

        if (videoTitle.equals("")){
            videoTitle = originTitle;
        }
        if(response.updateIsReady()){
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(videoDao.updateVideo(vid, videoTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis()));
            if (status.equals("UPDATE_SUCCESS")){
                return 200+"";
            }else {
                return 503+"";
            }
        }else{
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
            return 502+"";
        }
    }

    @RequestMapping(value = "/admin/tutorial/update", method = RequestMethod.POST)
    public @ResponseBody String updateTutorial(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("tutorialspeed");
        String videoDifficult = request.getParameter("tutorialdifficult");
        String videoStyle = request.getParameter("tutorialstyle");
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imageKey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle);

        int duplicateCode = checkTutorialTitleAuthorIdDuplicate(videoTitle, authorName);
        if (duplicateCode == 0){
            return 501+"";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = educationDao.updateEducationCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficult);
        categoryTitles.add(videoStyle);

        String newTutorialKey = educationDao.getEducationVideoById(vid).getVideo_key().split("\\.")[0] + "." + videoType;
        educationDao.updateVideoKeyById(vid, newTutorialKey);

        if (videoTitle.equals("")){
            videoTitle = originTitle;
        }
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(educationDao.updateEducation(vid, videoTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis()));
            if (status.equals("UPDATE_SUCCESS")) {
                return 200 + "";
            } else {
                return 503 + "";
            }
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
            return 505 + "";
        }
    }

    @RequestMapping(value = "/admin/music/update", method = RequestMethod.POST)
    public @ResponseBody String updateMusic(HttpServletRequest request, HttpSession session) {
        String musicTitle = request.getParameter("title");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        String musicLetter = request.getParameter("musicletter").toUpperCase();
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imageKey + " " + musicBeat + " " + musicStyle + " " + musicLetter);

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(musicLetter);
        if (isSingleLetter){
            System.out.println("是单个大写字母");
        }else{
            System.out.println("不是单个大写字母");
            return 505+"";
        }

        int duplicateCode = checkMusicTitleAuthorIdDuplicate(musicTitle, authorName);
        if (duplicateCode == 0){
            return 501+"";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = musicDao.updateMusicCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(musicBeat);
        categoryTitles.add(musicStyle);
        categoryTitles.add(musicLetter.toUpperCase());
        if (musicTitle.equals("")){
            musicTitle = originTitle;
        }
        if(response.updateIsReady()){
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(musicDao.updateMusic(vid, musicTitle, authorName, imageKey,categoryTitles,System.currentTimeMillis()));
            if (status.equals("UPDATE_SUCCESS")) {
                return 200 + "";
            } else {
                return 503 + "";
            }
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
            return 501 + "";
        }
    }

    @RequestMapping(value = "/admin/author/update", method = RequestMethod.POST)
    public @ResponseBody String updateAuthor(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");

        if (name.equals("")){
            name = (String)session.getAttribute("authorname");
        }

        if (description.equals("")){
            description = (String)session.getAttribute("authordescription");
        }

        System.out.println("requests: " + name + " " + description);

        Integer id = Integer.parseInt(request.getParameter("id"));
        int res = authorDao.updateAuthor(id, name, description, imagekey);//更新id为2的Author对象的名字
        String status = CRUDEvent.getResponse(res);
        if (status.equals("UPDATE_SUCCESS")) {
            return 200 + "";
        } else {
            return 503 + "";
        }
    }

    @RequestMapping(value = "/admin/team/update", method = RequestMethod.POST)
    public @ResponseBody String updateTeam(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");

        if (name.equals("")){
            name = (String)session.getAttribute("teamname");
        }

        if (description.equals("")){
            description = (String)session.getAttribute("teamdescription");
        }

        System.out.println("requests: " + name + " " + description + " " + imagekey);

        Integer id = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = danceDao.updateDanceGroupCheck(id, imagekey);
        if(response.updateIsReady()){
            String status = CRUDEvent.getResponse(danceDao.updateDanceGourp(id, name, description, imagekey, System.currentTimeMillis()));
            if (status.equals("UPDATE_SUCCESS")) {
                return 200 + "";
            } else {
                return 503 + "";
            }
        }else{
            System.out.println("请先完成舞队图片的插入");
            return 505 + "";
        }
    }
}
