package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.MusicCates;
import com.darfoo.backend.service.responsemodel.TutorialCates;
import com.darfoo.backend.service.responsemodel.VideoCates;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-4.
 * 用于展示已经上传的伴奏，视频和教学视频，全部或者是单个
 */

@Controller
public class GalleryController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    VideoCates videoCates;
    @Autowired
    TutorialCates tutorialCates;
    @Autowired
    MusicCates musicCates;

    @RequestMapping(value = "/admin/gallery/{type}/all", method = RequestMethod.GET)
    public String showAllResource(@PathVariable String type, ModelMap modelMap) {
        System.out.println("gallery -> " + type);
        List resources = commonDao.getAllResource(TypeClassMapping.typeClassMap.get(type));
        modelMap.addAttribute(String.format("all%ss", type), resources);
        return String.format("all%s", type);
    }

    @RequestMapping(value = "/admin/video/{id}", method = RequestMethod.GET)
    public String showSingleVideo(@PathVariable Integer id, ModelMap modelMap) {
        Video video = (Video) commonDao.getResourceById(Video.class, id);
        Set<VideoCategory> categories = video.getCategories();
        for (VideoCategory category : categories) {
            String categorytitle = category.getTitle();
            System.out.println(categorytitle);
            if (videoCates.getSpeedCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("speed", categorytitle);
            } else if (videoCates.getDifficultyCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("difficult", categorytitle);
            } else if (videoCates.getStyleCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("style", categorytitle);
            } else {
                modelMap.addAttribute("letter", categorytitle);
            }
        }
        String videoKey = video.getVideo_key();
        String videoType = videoKey.split("\\.")[1];
        modelMap.addAttribute("videotype", videoType);
        modelMap.addAttribute("video", video);
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(video.getImage().getImage_key()));
        if (video.getMusic() != null) {
            String connectmusic = video.getMusic().getTitle() + "-" + video.getMusic().getAuthorname();
            modelMap.addAttribute("connectmusic", connectmusic);
        } else {
            modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
        }
        return "singlevideo";
    }

    @RequestMapping(value = "/admin/tutorial/{id}", method = RequestMethod.GET)
    public String showSingleTutorial(@PathVariable Integer id, ModelMap modelMap) {
        Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, id);
        Set<TutorialCategory> categories = tutorial.getCategories();
        for (TutorialCategory category : categories) {
            String categorytitle = category.getTitle();
            System.out.println(categorytitle);
            if (tutorialCates.getSpeedCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("speed", categorytitle);
            } else if (tutorialCates.getDifficultyCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("difficult", categorytitle);
            } else if (tutorialCates.getStyleCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("style", categorytitle);
            } else {
                System.out.println("something is wrong with the category");
            }
        }
        String videoKey = tutorial.getVideo_key();
        String videoType = videoKey.split("\\.")[1];
        modelMap.addAttribute("videotype", videoType);
        modelMap.addAttribute("tutorial", tutorial);
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(tutorial.getImage().getImage_key()));
        if (tutorial.getMusic() != null) {
            String connectmusic = tutorial.getMusic().getTitle() + "-" + tutorial.getMusic().getAuthorname();
            modelMap.addAttribute("connectmusic", connectmusic);
        } else {
            modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
        }
        return "singletutorial";
    }

    @RequestMapping(value = "/admin/music/{id}", method = RequestMethod.GET)
    public String showSingleMusic(@PathVariable Integer id, ModelMap modelMap) {
        Music music = (Music) commonDao.getResourceById(Music.class, id);
        Set<MusicCategory> categories = music.getCategories();
        for (MusicCategory category : categories) {
            String categorytitle = category.getTitle();
            System.out.println(categorytitle);
            if (musicCates.getBeatCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("beat", categorytitle);
            } else if (musicCates.getStyleCategory().containsValue(categorytitle)) {
                modelMap.addAttribute("style", categorytitle);
            } else {
                modelMap.addAttribute("letter", categorytitle);
            }
        }
        modelMap.addAttribute("music", music);
        return "singlemusic";
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
}