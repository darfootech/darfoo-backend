package com.darfoo.backend.service;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.service.responsemodel.IndexDanceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 14-11-26.
 */

@Controller
@RequestMapping("/resources/team")
public class DanceTeamController {
    @Autowired
    DanceDao danceDao;

    @RequestMapping("/index")
    public
    @ResponseBody
    List<IndexDanceGroup> getIndexDanceGroups(){
        List<DanceGroup> groups = danceDao.getDanceGroups();
        List<IndexDanceGroup> result = new ArrayList<IndexDanceGroup>();
        for (DanceGroup group : groups){
            int id = group.getId();
            String image_url = group.getImage().getImage_key();
            String title = group.getName();
            Long update_timestamp = group.getUpdate_timestamp();
            result.add(new IndexDanceGroup(id, image_url, title, update_timestamp));
        }
        return result;
    }
}
