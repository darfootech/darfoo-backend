package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.model.cota.ModelUpdate;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by zjh on 14-12-4.
 */

@Controller
public class UpdateController {
    @Autowired
    DanceGroupDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public int commonUpdateResource(Class resource, HttpServletRequest request) {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        Integer id = Integer.parseInt(request.getParameter("id"));

        for (Field field : resource.getDeclaredFields()) {
            if (field.isAnnotationPresent(ModelUpdate.class)) {
                String insertkey = field.getName().toLowerCase();
                updatecontents.put(insertkey, request.getParameter(insertkey));
            }
        }

        if (resource == DanceVideo.class) {
            updatecontents.put("category1", request.getParameter("tutorialspeed"));
            updatecontents.put("category2", request.getParameter("tutorialdifficult"));
            updatecontents.put("category3", request.getParameter("tutorialstyle"));
        }

        if (resource == DanceMusic.class) {
            updatecontents.put("category1", request.getParameter("musicbeat"));
            updatecontents.put("category2", request.getParameter("musicstyle"));
            updatecontents.put("category3", request.getParameter("musicletter").toUpperCase());
        }

        HashMap<String, Integer> result = commonDao.updateResource(resource, id, updatecontents);
        int statuscode = result.get("statuscode");

        System.out.println("status code is -> " + statuscode);

        return statuscode;
    }

    @RequestMapping(value = "/admin/{type}/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer updateResource(@PathVariable String type, HttpServletRequest request) {
        return commonUpdateResource(TypeClassMapping.typeClassMap.get(type), request);
    }

    @RequestMapping(value = "/admin/{type}/updateimage/{id}", method = RequestMethod.GET)
    public String updateVideoImage(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap) {
        modelMap.addAttribute("resourceid", id);
        modelMap.addAttribute("type", type);
        return "update/updateresourceimage";
    }

    @RequestMapping(value = "/admin/{type}/updateimageresource", method = RequestMethod.POST)
    public String updateVideoImageResource(@PathVariable String type, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();

        System.out.println(id + " " + imagekey);

        String imageStatusCode = ServiceUtils.reUploadSmallResource(imageresource, imagekey);

        if (imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
}