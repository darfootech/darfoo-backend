package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources/category")
public class CategoryController {
    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public @ResponseBody
    VideoCategory getSingleVideoCategory(@PathVariable String id){
        VideoCategory targetVideoCategory = new VideoCategory();
        targetVideoCategory.setVideos(new Video[]{new Video(), new Video(), new Video()});
        return targetVideoCategory;
    }

    @RequestMapping("/video/all")
    public @ResponseBody
    VideoCategory[] getAllVideoCategory(){
        VideoCategory[] categorie = { new VideoCategory(), new VideoCategory(), new VideoCategory() };
        return categorie;
    }

    @RequestMapping(value = "/music/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MusicCategory getSingleVideo(@PathVariable String id){
        MusicCategory targetMusicCategory = new MusicCategory();
        targetMusicCategory.setMusics(new Music[]{new Music(), new Music(), new Music()});
        return targetMusicCategory;
    }

    @RequestMapping("/music/all")
    public @ResponseBody
    MusicCategory[] getAllMusicCategory(){
        MusicCategory[] categorie = { new MusicCategory(), new MusicCategory(), new MusicCategory() };
        return categorie;
    }
}
