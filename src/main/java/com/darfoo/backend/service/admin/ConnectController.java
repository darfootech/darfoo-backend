package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zjh on 14-12-13.
 */

@Controller
public class ConnectController {
    @Autowired
    MusicDao musicDao;
    @Autowired
    VideoDao videoDao;

    @RequestMapping(value = "/admin/connectmusic/all", method = RequestMethod.GET)
    public String connectMusicAll(ModelMap modelMap){
        modelMap.addAttribute("musics", musicDao.getAllMusic());
        return "connectionallmusic";
    }

    @RequestMapping(value = "/admin/connectmusic/single/{id}", method = RequestMethod.GET)
    public String connectMusicSingle(@PathVariable String id, ModelMap modelMap){
        Music music = musicDao.getMusicByMusicId(Integer.parseInt(id));
        modelMap.addAttribute("music", music);
        modelMap.addAttribute("videos", videoDao.getAllVideo());
        return "connectionsinglemusic";
    }
}
