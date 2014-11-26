package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources/music")
public class MusicController {
    @Autowired
    MusicDao musicDao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleMusic getSingleMusic(@PathVariable String id){
        Music targetMusic = musicDao.getMusicByMusicId(Integer.parseInt(id));
        int music_id = targetMusic.getId();
        String music_url = targetMusic.getMusic_key();
        String title = targetMusic.getTitle();
        return new SingleMusic(music_id, music_url, title);
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
