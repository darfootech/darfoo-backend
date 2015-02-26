package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.dao.resource.VideoDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by zjh on 14-12-13.
 */

@Controller
public class ConnectController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    @RequestMapping(value = "/admin/connectmusic/{type}/all", method = RequestMethod.GET)
    public String connectMusicAll(@PathVariable String type, ModelMap modelMap) {
        modelMap.addAttribute("musics", commonDao.getAllResource(Music.class));
        modelMap.addAttribute("type", type);
        return "connectmusic/connectionallmusic";
    }

    @RequestMapping(value = "/admin/connectmusic/single/{type}/{id}", method = RequestMethod.GET)
    public String connectMusicSingle(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        System.out.println("current music id: " + id);
        Music music = (Music) commonDao.getResourceById(Music.class, id);
        session.setAttribute("connectmusicid", id);
        modelMap.addAttribute("music", music);

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("music_id", id);

        Class resource = TypeClassMapping.typeClassMap.get(type);

        modelMap.addAttribute("connectvideos", commonDao.getResourcesByFields(resource, conditions));
        modelMap.addAttribute("notconnectvideos", accompanyDao.getResourcesWithoutMusicId(resource, id));
        modelMap.addAttribute("type", type);
        return "connectmusic/connectionsinglemusic";
    }

    @RequestMapping(value = "/admin/connectmusic/{operation}connects/{type}", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Integer addConnections(@PathVariable String type, @PathVariable String operation, HttpServletRequest request, HttpSession session) {
        String ids = request.getParameter("ids");
        System.out.println(ids);
        int currentmusicid = (Integer) session.getAttribute("connectmusicid");
        System.out.println("current music id: " + currentmusicid);
        String[] idArray = ids.split(",");

        Class resource = TypeClassMapping.typeClassMap.get(type);

        for (int i = 0; i < idArray.length; i++) {
            int status;
            if (operation.equals("add")) {
                status = accompanyDao.updateResourceMusic(resource, Integer.parseInt(idArray[i]), currentmusicid);
            } else if (operation.equals("del")){
                status = accompanyDao.deleteMusicFromResource(resource, Integer.parseInt(idArray[i]));
            } else {
                status = CRUDEvent.UPDATE_FAIL;
            }
            if (status != CRUDEvent.UPDATE_SUCCESS && status != CRUDEvent.DELETE_SUCCESS) {
                return 500;
            }
        }
        return 200;
    }
}
