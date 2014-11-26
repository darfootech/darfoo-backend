package com.darfoo.backend.service;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.service.responsemodel.IndexDanceGroup;
import com.darfoo.backend.utils.QiniuUtils;
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

    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping("/index")
    public
    @ResponseBody
    List<IndexDanceGroup> getIndexDanceGroups(){
        List<DanceGroup> groups = danceDao.getDanceGroups();
        List<IndexDanceGroup> result = new ArrayList<IndexDanceGroup>();
        for (DanceGroup group : groups){
            int id = group.getId();
            String image_url = group.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String title = group.getName();
            Long update_timestamp = group.getUpdate_timestamp();
            result.add(new IndexDanceGroup(id, image_download_url, title, update_timestamp));
        }
        return result;
    }
}
