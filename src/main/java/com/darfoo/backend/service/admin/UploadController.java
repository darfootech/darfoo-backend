package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.*;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.*;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
    TutorialDao tutorialDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public HashMap<String, Integer> insertSingleVideo(String videotitle, String videotype, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle, String videoletter) {
        System.out.println(authorname);

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Author targetAuthor = (Author) commonDao.getResourceByTitleOrName(Author.class, authorname, "name");
        if (targetAuthor != null) {
            System.out.println(targetAuthor.getName());
        } else {
            System.out.println("无该author记录");
            resultMap.put("statuscode", 501);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoletter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        int authorid = targetAuthor.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", videotitle);
        conditions.put("author_id", authorid);

        Video queryVideo = (Video) commonDao.getResourceByFields(Video.class, conditions);

        if (queryVideo == null) {
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        if (imagekey.equals("")) {
            resultMap.put("statuscode", 508);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", imagekey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);

        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
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
        if (insertStatus == -1) {
            System.out.println("插入视频失败");
        } else {
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", videotitle + "-" + insertStatus + "." + videotype);
        commonDao.updateResourceFieldsById(Video.class, insertStatus, updateMap);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        return resultMap;
    }

    public HashMap<String, Integer> insertSingleTutorialVideo(String videotitle, String videotype, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle) {
        System.out.println(authorname);

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Author targetAuthor = (Author) commonDao.getResourceByTitleOrName(Author.class, authorname, "name");
        if (targetAuthor != null) {
            System.out.println(targetAuthor.getName());
        } else {
            System.out.println("无该author记录");
            resultMap.put("statuscode", 501);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        int authorid = targetAuthor.getId();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", videotitle);
        conditions.put("author_id", authorid);

        Tutorial queryVideo = (Tutorial) commonDao.getResourceByFields(Tutorial.class, conditions);

        if (queryVideo == null) {
            System.out.println("教程和作者id组合不存在，可以进行插入");
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("教程和作者id组合已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        if (imagekey.equals("")) {
            resultMap.put("statuscode", 508);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", imagekey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Tutorial video = new Tutorial();
        video.setAuthor(targetAuthor);
        Image img = new Image();
        img.setImage_key(imagekey);
        video.setImage(img);
        TutorialCategory speed = new TutorialCategory();
        TutorialCategory difficult = new TutorialCategory();
        TutorialCategory style = new TutorialCategory();
        speed.setTitle(videospeed);
        difficult.setTitle(videodifficult);
        style.setTitle(videostyle);
        Set<TutorialCategory> s_eCategory = video.getCategories();
        s_eCategory.add(speed);
        s_eCategory.add(difficult);
        s_eCategory.add(style);
        video.setTitle(videotitle);
        video.setVideo_key(videotitle);
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = tutorialDao.insertSingleTutorial(video);
        if (insertStatus == -1) {
            System.out.println("插入教程失败");
        } else {
            System.out.println("插入教程成功，教程id是" + insertStatus);
        }

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", videotitle + "-" + insertStatus + "." + videotype);
        commonDao.updateResourceFieldsById(Tutorial.class, insertStatus, updateMap);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);

        return resultMap;
    }

    public HashMap<String, Integer> insertSingleMusic(String musictitle, String authorname, String imagekey, String musicbeat, String musicstyle, String musicletter) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(musicletter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        //伴奏title可以重名,但是不可能出现authorname和title都一样的情况,也就是一个作者名字对应的伴奏中不会出现重名的情况

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", musictitle);
        conditions.put("author_name", authorname);

        Music queryMusic = (Music) commonDao.getResourceByFields(Music.class, conditions);
        if (queryMusic == null) {
            System.out.println("伴奏名字和作者名字组合不存在，可以进行插入");
        } else {
            //System.out.println(queryMusic.toString());
            System.out.println("伴奏名字和作者名字组合已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Music music = new Music();

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
        music.setAuthorName(authorname);
        music.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = musicDao.insertSingleMusic(music);
        if (insertStatus == -1) {
            System.out.println("插入伴奏失败");
        } else {
            System.out.println("插入伴奏成功，伴奏id是" + insertStatus);
        }

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("music_key", musictitle + "-" + insertStatus);
        commonDao.updateResourceFieldsById(Music.class, insertStatus, updateMap);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        return resultMap;
    }

    public int insertSingleAuthor(String authorname, String description, String imagekey) {
        if (authorDao.isExistAuthor(authorname)) {
            System.out.println("作者已存在");
            return 501;
        } else {
            System.out.println("无该author记录，可以创建");
        }

        if (imagekey.equals("")) {
            return 508;
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", imagekey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return 503;
        }

        Author author = new Author();
        author.setName(authorname);
        author.setDescription(description);
        author.setImage(image);
        authorDao.insertAuthor(author);

        return 200;
    }

    /*video part*/
    @RequestMapping(value = "/resources/video/new", method = RequestMethod.GET)
    public String uploadVideo(ModelMap modelMap, HttpSession session) {
        session.setAttribute("resource", "video");
        modelMap.addAttribute("resource", "video");
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        return "uploadvideo";
    }

    @RequestMapping(value = "/resources/video/create", method = RequestMethod.POST)
    public
    @ResponseBody
    String createVideo(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        String connectmusic = request.getParameter("connectmusic");
        System.out.println("connectmusic -> " + connectmusic);

        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter + " " + update_timestamp);

        HashMap<String, Integer> resultMap = this.insertSingleVideo(videoTitle, videoType, authorName, imagekey, videoSpeed, videoDifficult, videoStyle, videoLetter);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200) {
            return statusCode + "";
        } else {
            int insertid = resultMap.get("insertid");
            session.setAttribute("videoKey", videoTitle + "-" + insertid + "." + videoType);
            session.setAttribute("videoImage", imagekey);

            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(Video.class, insertid, mid);
            }

            return statusCode + "";
        }
    }

    @RequestMapping(value = "/resources/videoresource/new", method = RequestMethod.GET)
    public String uploadVideoResource() {
        return "uploadvideoresource";
    }

    @RequestMapping("/resources/videoresource/create")
    public String createVideoResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String videoTitle = (String) session.getAttribute("videoKey");
        String imageKey = (String) session.getAttribute("videoImage");

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

        if (videoStatusCode.equals("200") && imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
    /*end of video part*/

    /*music part*/
    @RequestMapping(value = "/resources/music/new", method = RequestMethod.GET)
    public String uploadMusic(ModelMap modelMap, HttpSession session) {
        session.setAttribute("resource", "music");
        modelMap.addAttribute("resource", "music");
        return "uploadmusic";
    }

    @RequestMapping(value = "/resources/music/create", method = RequestMethod.POST)
    public
    @ResponseBody
    String createMusic(HttpServletRequest request, HttpSession session) {
        String musicTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        //String imagekey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        String musicLetter = request.getParameter("musicletter").toUpperCase();
        Long update_timestamp = System.currentTimeMillis() / 1000;
        //System.out.println("requests: " + musicTitle + " " + authorName + " " + imagekey + " " + musicBeat + " " + musicStyle + " " + musicLetter + " " + update_timestamp);
        System.out.println("requests: " + musicTitle + " " + authorName + " " + " " + musicBeat + " " + musicStyle + " " + musicLetter + " " + update_timestamp);

        //HashMap<String, Integer> resultMap = this.insertSingleMusic(musicTitle, authorName, imagekey, musicBeat, musicStyle, musicLetter);
        HashMap<String, Integer> resultMap = this.insertSingleMusic(musicTitle, authorName, "", musicBeat, musicStyle, musicLetter);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200) {
            return statusCode + "";
        } else {
            int insertid = resultMap.get("insertid");
            session.setAttribute("musicTitle", musicTitle + "-" + insertid + ".mp3");
            //session.setAttribute("musicImage", imagekey);
            return statusCode + "";
        }
    }

    @RequestMapping(value = "/resources/musicresource/new", method = RequestMethod.GET)
    public String uploadMusicResource() {
        return "uploadmusicresource";
    }

    @RequestMapping("/resources/musicresource/create")
    public String createMusicResource(@RequestParam("musicresource") CommonsMultipartFile musicresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String musicTitle = (String) session.getAttribute("musicTitle");
        String imageKey = (String) session.getAttribute("musicImage");

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

        if (musicStatusCode.equals("200") && imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }

    @RequestMapping("/resources/musicresourcenopic/create")
    public String createMusicResourceNoPic(@RequestParam("musicresource") CommonsMultipartFile musicresource, HttpSession session) {
        //upload
        String musicTitle = (String) session.getAttribute("musicTitle");

        String videoResourceName = musicresource.getOriginalFilename();

        System.out.println("musicresourcename -> " + videoResourceName);

        String musicStatusCode = "";

        try {
            musicStatusCode = ServiceUtils.uploadLargeResource(musicresource, musicTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (musicStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
    /*end of music part*/

    /*tutorial part*/
    @RequestMapping(value = "/resources/tutorial/new", method = RequestMethod.GET)
    public String uploadTutorial(ModelMap modelMap, HttpSession session) {
        session.setAttribute("resource", "tutorial");
        modelMap.addAttribute("resource", "tutorial");
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        return "uploadtutorial";
    }

    @RequestMapping(value = "/resources/tutorial/create", method = RequestMethod.POST)
    public
    @ResponseBody
    String createTutorial(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        String connectmusic = request.getParameter("connectmusic");
        System.out.println("connectmusic -> " + connectmusic);

        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);

        HashMap<String, Integer> resultMap = this.insertSingleTutorialVideo(videoTitle, videoType, authorName, imagekey, videoSpeed, videoDifficult, videoStyle);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200) {
            return statusCode + "";
        } else {
            int insertid = resultMap.get("insertid");
            session.setAttribute("tutorialKey", videoTitle + "-" + insertid + "." + videoType);
            session.setAttribute("tutorialImage", imagekey);

            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(Tutorial.class, insertid, mid);
            }

            return statusCode + "";
        }
    }

    @RequestMapping(value = "/resources/tutorialresource/new", method = RequestMethod.GET)
    public String uploadTutorialResource() {
        return "uploadtutorialresource";
    }

    @RequestMapping("/resources/tutorialresource/create")
    public String createTutorialResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String tutorialTitle = (String) session.getAttribute("tutorialKey");
        String imagekey = (String) session.getAttribute("tutorialImage");

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

        if (videoStatusCode.equals("200") && imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
    /*end of tutorial part*/

    /*author part*/
    //作者信息，不管是视频作者还是伴奏作者
    @RequestMapping(value = "/resources/author/new", method = RequestMethod.GET)
    public String uploadAuthor(ModelMap modelMap, HttpSession session) {
        session.setAttribute("resource", "author");
        modelMap.addAttribute("resource", "author");
        return "uploadauthor";
    }

    @RequestMapping(value = "/resources/author/create", method = RequestMethod.POST)
    public
    @ResponseBody
    String createAuthor(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        System.out.println("requests: " + name + " " + description);

        session.setAttribute("authorImage", imagekey);

        int statusCode = this.insertSingleAuthor(name, description, imagekey);
        return statusCode + "";
    }

    @RequestMapping(value = "/resources/authorresource/new", method = RequestMethod.GET)
    public String uploadAuthorResource() {
        return "uploadauthorresource";
    }

    @RequestMapping("/resources/authorresource/create")
    public String createAuthorResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String imagekey = (String) session.getAttribute("authorImage");
        System.out.println("imagekey in session: " + imagekey);

        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(imageResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
    /*end of author part*/
}
