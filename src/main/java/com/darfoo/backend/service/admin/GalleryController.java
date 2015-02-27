package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.*;
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
        List resources = commonDao.getAllResource(TypeClassMapping.typeClassMap.get(type));
        modelMap.addAttribute("resources", resources);
        modelMap.addAttribute("type", type);
        return "resource/allresources";
    }

    private ModelMap getResourceModelMap(Class resource, Integer id, ModelMap modelMap) {
        Object object = commonDao.getResourceById(resource, id);
        if (resource == Video.class) {
            Video video = (Video) object;
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
        } else if (resource == Tutorial.class) {
            Tutorial tutorial = (Tutorial) object;
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
        } else if (resource == Music.class) {
            Music music = (Music) object;
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
        }

        if (resource == Video.class || resource == Tutorial.class) {
            String videotype = commonDao.getResourceAttr(resource, object, "video_key").toString().split("\\.")[1];
            String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
            Music music = (Music) commonDao.getResourceAttr(resource, object, "music");
            modelMap.addAttribute("videotype", videotype);
            modelMap.addAttribute("video", object);
            modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
            modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(imagekey));
            if (music != null) {
                String connectmusic = music.getTitle() + "-" + music.getAuthorname();
                modelMap.addAttribute("connectmusic", connectmusic);
            } else {
                modelMap.addAttribute("connectmusic", "请输入要关联的伴奏并选择");
            }
        }

        if (resource == Music.class) {
            modelMap.addAttribute("music", object);
        }

        if (resource == Author.class) {
            modelMap.addAttribute("author", object);
            Image image = (Image) commonDao.getResourceAttr(resource, object, "image");
            if (image != null) {
                modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(image.getImage_key()));
                if (image.getImage_key().equals("")) {
                    modelMap.addAttribute("updateauthorimage", 1);
                }
            } else {
                modelMap.addAttribute("imageurl", "");
            }
        }

        return modelMap;
    }

    @RequestMapping(value = "/admin/{type}/{id}", method = RequestMethod.GET)
    public String showSingleResource(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap) {
        getResourceModelMap(TypeClassMapping.typeClassMap.get(type), id, modelMap);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("type", type);
        return String.format("resource/single%s", type);
    }
}