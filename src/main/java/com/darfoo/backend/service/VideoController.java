package com.darfoo.backend.service;

import com.darfoo.backend.model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.awt.SunHints;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zjh on 14-11-16.
 */

@Controller
@RequestMapping("/resources/video")
public class VideoController {
    //http://localhost:8080/darfoobackend/rest/resources/video/3
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    Video getSingleVideo(@PathVariable String id){
        Video targetVideo = new Video();
        return targetVideo;
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
