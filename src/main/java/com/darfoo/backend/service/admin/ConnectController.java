package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CommonDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Music;
import org.hibernate.Session;
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
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "/admin/connectmusic/all", method = RequestMethod.GET)
    public String connectMusicAll(ModelMap modelMap) {
        modelMap.addAttribute("musics", musicDao.getAllMusic());
        return "connectionallmusic";
    }

    @RequestMapping(value = "/admin/connectmusic/single/{id}", method = RequestMethod.GET)
    public String connectMusicSingle(@PathVariable String id, ModelMap modelMap, HttpSession session) {
        int musicid = Integer.parseInt(id);
        System.out.println("current music id: " + musicid);
        Music music = (Music) commonDao.getResourceById(Music.class, musicid);
        session.setAttribute("connectmusicid", musicid);
        modelMap.addAttribute("music", music);
        modelMap.addAttribute("connectvideos", videoDao.getVideosByMusicId(musicid));
        modelMap.addAttribute("notconnectvideos", videoDao.getVideosWithoutMusicId(musicid));
        return "connectionsinglemusic";
    }

    @RequestMapping(value = "/admin/connectmusic/addconnects", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String addConnections(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        int currentmusicid = (Integer) session.getAttribute("connectmusicid");
        System.out.println("current music id: " + currentmusicid);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            videoDao.insertOrUpdateMusic(Integer.parseInt(videoids[i]), currentmusicid);
        }
        return 200 + "";
    }

    @RequestMapping(value = "/admin/connectmusic/delconnects", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String delConnections(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        int currentmusicid = (Integer) session.getAttribute("connectmusicid");
        System.out.println("current music id: " + currentmusicid);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            videoDao.disconnectVideoMusic(Integer.parseInt(videoids[i]), currentmusicid);
        }
        return 200 + "";
    }

}
