package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    TutorialDao educationDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    DanceDao danceDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;

    @RequestMapping(value = "/admin/video/all", method = RequestMethod.GET)
    public String showAllVideo(ModelMap modelMap, HttpSession session) {
        List<Video> s_videos = new ArrayList<Video>();
        s_videos = videoDao.getAllVideo();
        modelMap.addAttribute("allvideos", s_videos);
        return "allvideo";
    }

    @RequestMapping(value = "/admin/video/{id}", method = RequestMethod.GET)
    public String showSingleVideo(@PathVariable Integer id, ModelMap modelMap) {
        Video video = (Video) commonDao.getResourceById(Video.class, id);
        Set<VideoCategory> categories = video.getCategories();
        for (VideoCategory category : categories) {
            int categoryid = category.getId();
            String categorytitle = category.getTitle();
            System.out.println(categoryid);
            System.out.println(categorytitle);
            if (categoryid >= 1 && categoryid <= 3) {
                modelMap.addAttribute("speed", category.getTitle());
            } else if (categoryid >= 4 && categoryid <= 6) {
                modelMap.addAttribute("difficult", category.getTitle());
            } else if (categoryid >= 7 && categoryid <= 17) {
                modelMap.addAttribute("style", category.getTitle());
            } else if (categoryid >= 18 && categoryid <= 43) {
                modelMap.addAttribute("letter", category.getTitle());
            } else {
                System.out.println("something is wrong with the category");
            }
        }
        String videoKey = video.getVideo_key();
        String videoType = videoKey.split("\\.")[1];
        modelMap.addAttribute("videotype", videoType);
        modelMap.addAttribute("video", video);
        modelMap.addAttribute("authors", authorDao.getAllAuthor());
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(video.getImage().getImage_key()));
        if (video.getMusic() != null) {
            String connectmusic = video.getMusic().getTitle() + "-" + video.getMusic().getAuthorName();
            modelMap.addAttribute("connectmusic", connectmusic);
        } else {
            modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
        }
        return "singlevideo";
    }

    @RequestMapping(value = "/admin/tutorial/all", method = RequestMethod.GET)
    public String showAllTutorial(ModelMap modelMap, HttpSession session) {
        List<Tutorial> s_tutorial = new ArrayList<Tutorial>();
        s_tutorial = educationDao.getAllEducation();
        modelMap.addAttribute("alltutorials", s_tutorial);
        return "alltutorial";
    }

    @RequestMapping(value = "/admin/tutorial/{id}", method = RequestMethod.GET)
    public String showSingleTutorial(@PathVariable Integer id, ModelMap modelMap) {
        Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, id);
        Set<TutorialCategory> categories = tutorial.getCategories();
        for (TutorialCategory category : categories) {
            int categoryid = category.getId();
            String categorytitle = category.getTitle();
            System.out.println(categoryid);
            System.out.println(categorytitle);
            if (categorytitle.equals("快") || categorytitle.equals("中") || categorytitle.equals("慢")) {
                modelMap.addAttribute("speed", categorytitle);
            } else if (categorytitle.equals("简单") || categorytitle.equals("适中") || categorytitle.equals("稍难")) {
                modelMap.addAttribute("difficult", categorytitle);
            } else if (categorytitle.equals("背面教学") || categorytitle.equals("分解教学") || categorytitle.equals("队形表演")) {
                modelMap.addAttribute("style", category.getTitle());
            } else {
                System.out.println("something is wrong with the category");
            }
        }
        String videoKey = tutorial.getVideo_key();
        String videoType = videoKey.split("\\.")[1];
        modelMap.addAttribute("videotype", videoType);
        modelMap.addAttribute("tutorial", tutorial);
        modelMap.addAttribute("authors", authorDao.getAllAuthor());
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(tutorial.getImage().getImage_key()));
        if (tutorial.getMusic() != null) {
            String connectmusic = tutorial.getMusic().getTitle() + "-" + tutorial.getMusic().getAuthorName();
            modelMap.addAttribute("connectmusic", connectmusic);
        } else {
            modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
        }
        return "singletutorial";
    }

    @RequestMapping(value = "/admin/music/all", method = RequestMethod.GET)
    public String showAllMusic(ModelMap modelMap, HttpSession session) {
        List<Music> s_music = new ArrayList<Music>();
        s_music = musicDao.getAllMusic();
        modelMap.addAttribute("allmusics", s_music);
        return "allmusic";
    }

    @RequestMapping(value = "/admin/music/{id}", method = RequestMethod.GET)
    public String showSingleMusic(@PathVariable Integer id, ModelMap modelMap) {
        Music music = (Music) commonDao.getResourceById(Music.class, id);
        Set<MusicCategory> categories = music.getCategories();
        for (MusicCategory category : categories) {
            int categoryid = category.getId();
            String categorytitle = category.getTitle();
            System.out.println(categoryid);
            System.out.println(categorytitle);
            if (categoryid >= 1 && categoryid <= 4) {
                modelMap.addAttribute("beat", category.getTitle());
            } else if (categoryid >= 5 && categoryid <= 12) {
                modelMap.addAttribute("style", category.getTitle());
            } else if (categoryid >= 13 && categoryid <= 38) {
                modelMap.addAttribute("letter", category.getTitle());
            } else {
                System.out.println("something is wrong with the category");
            }
        }
        modelMap.addAttribute("music", music);
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(music.getImage().getImage_key()));
        return "singlemusic";
    }

    @RequestMapping(value = "/admin/author/all", method = RequestMethod.GET)
    public String showAllAuthor(ModelMap modelMap, HttpSession session) {
        List<Author> s_author = new ArrayList<Author>();
        s_author = authorDao.getAllAuthor();
        modelMap.addAttribute("allauthors", s_author);
        return "allauthor";
    }

    @RequestMapping(value = "/admin/author/{id}", method = RequestMethod.GET)
    public String showSingleAuthor(@PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        Author author = (Author) commonDao.getResourceById(Author.class, id);

        session.setAttribute("authorname", author.getName());
        session.setAttribute("authordescription", author.getDescription());

        modelMap.addAttribute("author", author);
        if (author.getImage() != null) {
            modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(author.getImage().getImage_key()));
            if (author.getImage().getImage_key().equals("")) {
                modelMap.addAttribute("updateauthorimage", 1);
            }
        } else {
            modelMap.addAttribute("imageurl", "");
        }
        return "singleauthor";
    }

    @RequestMapping(value = "/admin/team/all", method = RequestMethod.GET)
    public String showAllDanceGroup(ModelMap modelMap, HttpSession session) {
        List<DanceGroup> s_team = new ArrayList<DanceGroup>();
        s_team = danceDao.getAllDanceGourp();
        modelMap.addAttribute("allteams", s_team);
        return "allteam";
    }

    @RequestMapping(value = "/admin/team/{id}", method = RequestMethod.GET)
    public String showSingleDanceGroup(@PathVariable String id, ModelMap modelMap, HttpSession session) {
        System.out.println(Integer.parseInt(id));
        DanceGroup danceGroup = danceDao.getTeamById(Integer.parseInt(id));

        session.setAttribute("teamname", danceGroup.getName());
        session.setAttribute("teamdescription", danceGroup.getDescription());

        modelMap.addAttribute("team", danceGroup);
        return "singleteam";
    }
}
