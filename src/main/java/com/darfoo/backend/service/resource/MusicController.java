package com.darfoo.backend.service.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-2-23.
 */

@Controller
@RequestMapping("/resources/music")
public class MusicController {
    @Autowired
    CommonDao commonDao;

    //视频资源需要关联伴奏 通过ajax获取所有伴奏数据 在后台界面上选择
    @RequestMapping(value = "/all/service", method = RequestMethod.GET)
    public
    @ResponseBody
    List<HashMap<String, Object>> getAllMusicService() {
        List<DanceMusic> musics = commonDao.getAllResource(DanceMusic.class);
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        for (DanceMusic music : musics) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("word", music.getTitle() + "-" + music.getAuthorname() + "-" + music.getId());
            result.add(item);
        }
        return result;
    }
}
