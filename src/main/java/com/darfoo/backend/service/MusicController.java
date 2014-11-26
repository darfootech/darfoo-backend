package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.service.responsemodel.HotMusic;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/hottest")
    public @ResponseBody
    List<HotMusic> getHottestMusics(){
        List<Music> musics = musicDao.getHottestMusics(5);
        List<HotMusic> result = new ArrayList<HotMusic>();
        for (Music music : musics){
            int id = music.getId();
            String title = music.getTitle();
            Long update_timestamp = music.getUpdate_timestamp();
            result.add(new HotMusic(id, title, update_timestamp));
        }
        return result;
    }
}
