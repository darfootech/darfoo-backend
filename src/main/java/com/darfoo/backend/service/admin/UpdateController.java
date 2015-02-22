package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.*;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.resource.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
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
    TutorialDao tutorialDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public int checkVideoTitleAuthorIdDuplicate(String videoTitle, String authorName) {
        Author a = (Author) commonDao.getResourceByTitleOrName(Author.class, authorName, "name");
        if (a != null) {
            System.out.println(a.getName());
        } else {
            System.out.println("无该author记录");
        }

        int authorid = a.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", videoTitle);
        conditions.put("author_id", authorid);

        Video queryVideo = (Video) commonDao.getResourceByFields(Video.class, conditions);
        if (queryVideo == null) {
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
            return 1;
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    public int checkTutorialTitleAuthorIdDuplicate(String tutorialTitle, String authorName) {
        Author a = (Author) commonDao.getResourceByTitleOrName(Author.class, authorName, "name");
        if (a != null) {
            System.out.println(a.getName());
        } else {
            System.out.println("无该author记录");
        }

        int authorid = a.getId();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", tutorialTitle);
        conditions.put("author_id", authorid);

        Tutorial queryVideo = (Tutorial) commonDao.getResourceByFields(Tutorial.class, conditions);
        if (queryVideo == null) {
            System.out.println("教程和作者id组合不存在，可以进行插入");
            return 1;
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("教程和作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    public int checkMusicTitleAuthorIdDuplicate(String musicTitle, String authorName) {
        Author a = (Author) commonDao.getResourceByTitleOrName(Author.class, authorName, "name");
        if (a != null) {
            System.out.println(a.getName());
        } else {
            System.out.println("无该author记录");
        }

        int authorid = a.getId();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", musicTitle);
        conditions.put("author_id", authorid);

        Music queryMusic = (Music) commonDao.getResourceByFields(Music.class, conditions);
        if (queryMusic == null) {
            System.out.println("伴奏与作者id组合不存在，可以进行插入");
            return 1;
        } else {
            System.out.println(queryMusic.getId());
            System.out.println(queryMusic.getAuthor().getName());
            System.out.println("伴奏与作者id组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    public int checkMusicTitleAuthorNameDuplicate(String musicTitle, String authorName) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", musicTitle);
        conditions.put("author_name", authorName);

        Music queryMusic = (Music) commonDao.getResourceByFields(Music.class, conditions);
        if (queryMusic == null) {
            System.out.println("伴奏名字和作者名字组合不存在，可以进行插入");
            return 1;
        } else {
            System.out.println(queryMusic.toString());
            System.out.println("伴奏名字和作者名字组合已存在，不可以进行插入了，是否需要修改");
            return 0;
        }
    }

    @RequestMapping(value = "/admin/video/update", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateVideo(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        String connectmusic = request.getParameter("connectmusic");
        System.out.println("connectmusic -> " + connectmusic);

        System.out.println("requests: " + videoTitle + " " + authorName + " " + imageKey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter);

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoLetter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            return 505 + "";
        }

        int duplicateCode = checkVideoTitleAuthorIdDuplicate(videoTitle, authorName);
        if (duplicateCode == 0) {
            return 501 + "";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = videoDao.updateVideoCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficult);
        categoryTitles.add(videoStyle);
        categoryTitles.add(videoLetter.toUpperCase());

        String newVideoKey = ((Video) commonDao.getResourceById(Video.class, vid)).getVideo_key().split("\\.")[0] + "." + videoType;
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", newVideoKey);
        commonDao.updateResourceFieldsById(Video.class, vid, updateMap);

        if (videoTitle.equals("")) {
            videoTitle = originTitle;
        }
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(videoDao.updateVideo(vid, videoTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis()));

            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(Video.class, vid, mid);
            }

            if (status.equals("UPDATE_SUCCESS")) {
                return 200 + "";
            } else {
                return 503 + "";
            }
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
            return 502 + "";
        }
    }

    @RequestMapping(value = "/admin/video/updateimage/{id}", method = RequestMethod.GET)
    public String updateVideoImage(@PathVariable String id, ModelMap modelMap) {
        modelMap.addAttribute("videoid", Integer.parseInt(id));
        return "updatevideoimage";
    }

    @RequestMapping(value = "/admin/video/updateimageresource", method = RequestMethod.POST)
    public String updateVideoImageResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String imagekey = ((Video) commonDao.getResourceById(Video.class, id)).getImage().getImage_key();

        System.out.println(id + " " + imagekey);

        ServiceUtils.deleteResource(imagekey);

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

    @RequestMapping(value = "/admin/tutorial/update", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateTutorial(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoType = request.getParameter("videotype");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("tutorialspeed");
        String videoDifficult = request.getParameter("tutorialdifficult");
        String videoStyle = request.getParameter("tutorialstyle");
        String connectmusic = request.getParameter("connectmusic");
        System.out.println("connectmusic -> " + connectmusic);

        System.out.println("requests: " + videoTitle + " " + authorName + " " + imageKey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle);

        int duplicateCode = checkTutorialTitleAuthorIdDuplicate(videoTitle, authorName);
        if (duplicateCode == 0) {
            return 501 + "";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = tutorialDao.updateTutorialCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficult);
        categoryTitles.add(videoStyle);

        String newTutorialKey = ((Tutorial) commonDao.getResourceById(Tutorial.class, vid)).getVideo_key().split("\\.")[0] + "." + videoType;

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", newTutorialKey);

        commonDao.updateResourceFieldsById(Tutorial.class, vid, updateMap);

        if (videoTitle.equals("")) {
            videoTitle = originTitle;
        }
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(tutorialDao.updateTutorial(vid, videoTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis()));

            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(Tutorial.class, vid, mid);
            }

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

    @RequestMapping(value = "/admin/tutorial/updateimage/{id}", method = RequestMethod.GET)
    public String updateTutorialImage(@PathVariable String id, ModelMap modelMap) {
        modelMap.addAttribute("tutorialid", Integer.parseInt(id));
        return "updatetutorialimage";
    }

    @RequestMapping(value = "/admin/tutorial/updateimageresource", method = RequestMethod.POST)
    public String updateTutorialImageResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String imagekey = ((Tutorial) commonDao.getResourceById(Tutorial.class, id)).getImage().getImage_key();

        System.out.println(id + " " + imagekey);

        ServiceUtils.deleteResource(imagekey);

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

    @RequestMapping(value = "/admin/music/update", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateMusic(HttpServletRequest request, HttpSession session) {
        String musicTitle = request.getParameter("title");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String imageKey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        String musicLetter = request.getParameter("musicletter").toUpperCase();
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imageKey + " " + musicBeat + " " + musicStyle + " " + musicLetter);

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(musicLetter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            return 505 + "";
        }

        int duplicateCode = checkMusicTitleAuthorIdDuplicate(musicTitle, authorName);
        if (duplicateCode == 0) {
            return 501 + "";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = musicDao.updateMusicCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(musicBeat);
        categoryTitles.add(musicStyle);
        categoryTitles.add(musicLetter.toUpperCase());
        if (musicTitle.equals("")) {
            musicTitle = originTitle;
        }
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(musicDao.updateMusic(vid, musicTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis()));
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

    @RequestMapping(value = "/admin/music/updatenopic", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateMusicNoPic(HttpServletRequest request, HttpSession session) {
        String musicTitle = request.getParameter("title");
        String originTitle = request.getParameter("origintitle");
        String authorName = request.getParameter("authorname");
        String originAuthorName = request.getParameter("originauthorname");
        String authorObjectName = request.getParameter("authorobjectname");
        String imageKey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        String musicLetter = request.getParameter("musicletter").toUpperCase();

        System.out.println("requests: " + musicTitle + " " + authorName + " " + imageKey + " " + musicBeat + " " + musicStyle + " " + musicLetter);

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(musicLetter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            return 505 + "";
        }

        int duplicateCode = checkMusicTitleAuthorNameDuplicate(musicTitle, authorName);
        if (duplicateCode == 0) {
            return 501 + "";
        }

        Integer vid = Integer.parseInt(request.getParameter("id"));
        UpdateCheckResponse response = musicDao.updateMusicCheck(vid, authorObjectName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(musicBeat);
        categoryTitles.add(musicStyle);
        categoryTitles.add(musicLetter.toUpperCase());

        if (musicTitle.equals("")) {
            musicTitle = originTitle;
        }
        if (authorName.equals("")) {
            authorName = originAuthorName;
        }

        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            String status = CRUDEvent.getResponse(musicDao.updateMusic(vid, musicTitle, authorObjectName, imageKey, categoryTitles, System.currentTimeMillis()));

            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("authorName", authorName);

            String updateAuthorNameStatus = CRUDEvent.getResponse(commonDao.updateResourceFieldsById(Music.class, vid, updateMap));

            if (status.equals("UPDATE_SUCCESS") && updateAuthorNameStatus.equals("UPDATE_SUCCESS")) {
                return 200 + "";
            } else {
                return 503 + "";
            }
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
            return 501 + "";
        }
    }

    @RequestMapping(value = "/admin/music/updateimage/{id}", method = RequestMethod.GET)
    public String updateMusicImage(@PathVariable String id, ModelMap modelMap) {
        modelMap.addAttribute("musicid", Integer.parseInt(id));
        return "updatemusicimage";
    }

    @RequestMapping(value = "/admin/music/updateimageresource", method = RequestMethod.POST)
    public String updateMusicImageResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String imagekey = ((Music) commonDao.getResourceById(Music.class, id)).getImage().getImage_key();

        System.out.println(id + " " + imagekey);

        ServiceUtils.deleteResource(imagekey);

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

    @RequestMapping(value = "/admin/author/update", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateAuthor(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        String newimagekey = request.getParameter("newimagekey");

        if (authorDao.isExistAuthor(name)) {
            System.out.println("作者已存在");
            return 501 + "";
        } else {
            System.out.println("无该author记录，可以创建");
        }

        if (newimagekey != null) {
            Image image = imageDao.getImageByName(newimagekey);
            if (image == null) {
                System.out.println("图片不存在，可以进行插入");
                image = new Image();
                image.setImage_key(newimagekey);
                imageDao.insertSingleImage(image);
                session.setAttribute("authorImage", newimagekey);
            } else {
                System.out.println("图片已存在，不可以进行插入了，是否需要修改");
                return 505 + "";
            }
        }

        if (name.equals("")) {
            name = (String) session.getAttribute("authorname");
        }

        if (description.equals("")) {
            description = (String) session.getAttribute("authordescription");
        }

        System.out.println("requests: " + name + " " + description);

        Integer id = Integer.parseInt(request.getParameter("id"));
        int res = authorDao.updateAuthor(id, name, description, newimagekey);//更新id为2的Author对象的名字
        String status = CRUDEvent.getResponse(res);
        if (status.equals("UPDATE_SUCCESS")) {
            if (newimagekey != null) {
                return 201 + "";
            } else {
                return 200 + "";
            }
        } else {
            return 503 + "";
        }
    }

    @RequestMapping(value = "/admin/author/updateimage/{id}", method = RequestMethod.GET)
    public String updateAuthorImage(@PathVariable String id, ModelMap modelMap) {
        modelMap.addAttribute("authorid", Integer.parseInt(id));
        return "updateauthorimage";
    }

    @RequestMapping(value = "/admin/author/updateimageresource", method = RequestMethod.POST)
    public String updateAuthorImageResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String imagekey = ((Author) commonDao.getResourceById(Author.class, id)).getImage().getImage_key();

        System.out.println(id + " " + imagekey);

        ServiceUtils.deleteResource(imagekey);

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
}
