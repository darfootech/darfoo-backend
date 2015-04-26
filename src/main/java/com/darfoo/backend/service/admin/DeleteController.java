package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Advertise;
import com.darfoo.backend.model.ThirdPartApp;
import com.darfoo.backend.model.Version;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zjh on 14-12-4.
 */

@Controller
public class DeleteController {
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "admin/{type}/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer deleteResource(@PathVariable String type, HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        if (resource == DanceVideo.class || resource == OperaVideo.class) {
            String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
            String videokey = commonDao.getResourceAttr(resource, object, "video_key").toString();
            ServiceUtils.deleteResource(imagekey);
            ServiceUtils.deleteResource(videokey);
        } else if (resource == DanceMusic.class) {
            String musickey = commonDao.getResourceAttr(resource, object, "music_key").toString();

            ServiceUtils.deleteResource(musickey);
        } else if (resource == DanceGroup.class || resource == OperaSeries.class || resource == Advertise.class) {
            Image image = (Image) commonDao.getResourceAttr(resource, object, "image");
            if (image != null) {
                String imagekey = image.getImage_key();
                ServiceUtils.deleteResource(imagekey);
            }
        } else if (resource == Version.class) {
            String versiontype = (String) commonDao.getResourceAttr(resource, object, "type");
            String versionnum = (String) commonDao.getResourceAttr(resource, object, "version");
            String versionkey = String.format("launcher-%s-%s.apk", versionnum, versiontype);
            ServiceUtils.deleteResource(versionkey);
        } else if (resource == ThirdPartApp.class) {
            String appkey = (String) commonDao.getResourceAttr(resource, object, "app_key");
            ServiceUtils.deleteResource(appkey);
        }
        String status = CRUDEvent.getResponse(commonDao.deleteResourceById(resource, id));
        if (status.equals("DELETE_SUCCESS")) {
            return 200;
        } else {
            return 505;
        }
    }
}