package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String connectMusicSingle(@PathVariable String id, ModelMap modelMap, HttpSession session){
        Music music = musicDao.getMusicByMusicId(Integer.parseInt(id));
        session.setAttribute("connectmusicid", Integer.parseInt(id));
        modelMap.addAttribute("music", music);
        modelMap.addAttribute("videos", videoDao.getAllVideo());
        return "connectionsinglemusic";
    }

    @RequestMapping(value = "/admin/connectmusic/addconnects", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody String addConnections(HttpServletRequest request){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        return 200+"";
    }

    @RequestMapping(value = "/admin/connectmusic/delconnects", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody String delConnections(HttpServletRequest request){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        return 200+"";
    }

}
