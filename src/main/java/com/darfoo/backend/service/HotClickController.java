package com.darfoo.backend.service;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zjh on 15-4-2.
 */

//增加点击量
@Controller
@RequestMapping(value = "/resources/hotclick")
public class HotClickController {
    @Autowired
    CommonDao commonDao;

    String hottestField = "hottest";

    @RequestMapping(value = "{type}/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    String singleResourceHotClick(@PathVariable String type, @PathVariable Integer id) {
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(TypeClassMapping.typeClassMap.get(type), id, hottestField)));
        return "ok";
    }

    @RequestMapping(value = "{type}/{accompanytype}/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    String accompanyResourceHotClick(@PathVariable String type, @PathVariable String accompanytype, @PathVariable Integer id) {
        if (type.equals("dancevideo") && accompanytype.equals("dancemusic")) {
            DanceMusic music = ((DanceVideo) commonDao.getResourceById(DanceVideo.class, id)).getMusic();
            if (music != null) {
                int musicid = music.getId();
                System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(DanceMusic.class, musicid, hottestField)));
            } else {
            }
        }
        return "ok";
    }
}
