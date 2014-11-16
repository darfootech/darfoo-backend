package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.model.Music;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources/music")
public class MusicController {
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    Music getSingleMusic(@PathVariable String id){
        Music targetMusic = new Music();
        return targetMusic;
    }

    @RequestMapping(value = "/playurl/{key}", method = RequestMethod.GET)
    public @ResponseBody
    String getPlayUrl(@PathVariable String key){
        return "http://playurl";
    }

    @RequestMapping("/hottest")
    public @ResponseBody
    Music[] getHottestMusics(){
        Music[] hottestMusics = { new Music(), new Music(), new Music() };
        return hottestMusics;
    }

    @RequestMapping("/index")
    public @ResponseBody
    Music[] getIndexMusics(){
        Music[] indexMusics = { new Music(), new Music(), new Music(), new Music(), new Music() };
        return indexMusics;
    }
}
