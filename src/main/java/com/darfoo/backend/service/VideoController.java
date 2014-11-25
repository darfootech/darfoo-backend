package com.darfoo.backend.service;

import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zjh on 14-11-16.
 */

@Controller
@RequestMapping("/resources/video")
public class VideoController {
    //http://localhost:8080/darfoobackend/rest/resources/video/3
    @Autowired
    VideoDao videoDao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo getSingleVideo(@PathVariable String id){
        Video targetVideo = videoDao.getVideoByVideoId(Integer.parseInt(id));
        //targetVideo.setCategories(new VideoCategory[] { new VideoCategory(), new VideoCategory(), new VideoCategory() });
        int videoid = targetVideo.getId();
        String video_url = targetVideo.getVideo_key();
        String title = targetVideo.getTitle();
        return new SingleVideo(videoid, title, video_url);
    }

    @RequestMapping(value = "/playurl/{key}", method = RequestMethod.GET)
    public @ResponseBody
    String getPlayUrl(@PathVariable String key){
        return "http://playurl";
    }

    @RequestMapping("/hottest")
    public @ResponseBody
    Video[] getHottestVideos(){
        Video[] hottestVideos = { new Video(), new Video(), new Video() };
        return hottestVideos;
    }

    @RequestMapping("/index")
    public @ResponseBody
    Video[] getIndexVideos(){
        Video[] indexVideos = { new Video(), new Video(), new Video(), new Video(), new Video() };
        return indexVideos;
    }
}
