package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.model.cota.annotations.ModelUpdate;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;
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
            String[] categories = request.getParameterValues("categories");
            if (categories == null) {
                updatecontents.put("category", "");
            } else {
                for (String category : categories) {
                    updatecontents.put(String.format("category%s", category), DanceVideoCates.danceVideoCategoryMap.get(category));
                }
            }
        }

        if (resource == DanceMusic.class) {
            updatecontents.put("category", request.getParameter("musicletter").toUpperCase());
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

    @RequestMapping(value = "/admin/{type}/update{resourcetype}/{id}", method = RequestMethod.GET)
    public String updateVideoImage(@PathVariable String type, @PathVariable String resourcetype, @PathVariable Integer id, ModelMap modelMap) {
        modelMap.addAttribute("resourceid", id);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("resourcetype", resourcetype);
        return "update/updateresourceimage";
    }

    @RequestMapping(value = "/admin/operavideo/updateorder", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer updateOperavideoOrder(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            int operavideoid = Integer.parseInt(name.split("-")[1]);
            int order = Integer.parseInt(value);
            System.out.println("operavideoid -> " + operavideoid);
            System.out.println("set order -> " + order);

            HashMap<String, Object> updatecontents = new HashMap<String, Object>();
            updatecontents.put("order", order);
            commonDao.updateResourceFieldsById(OperaVideo.class, operavideoid, updatecontents);
        }

        return 200;
    }

    @RequestMapping(value = "/admin/{type}/update{resourcetype}resource", method = RequestMethod.POST)
    public String updateVideoImageResource(@PathVariable String type, @PathVariable String resourcetype, @RequestParam("resourcefile") CommonsMultipartFile resourcefile, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        String resourcekey;
        ModelUploadEnum uploadtype;
        if (resourcetype.equals("image")) {
            uploadtype = ModelUploadEnum.SMALL;
            resourcekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
        } else if (resourcetype.equals("video") || resourcetype.equals("music")) {
            uploadtype = ModelUploadEnum.LARGE;
            resourcekey = (String) commonDao.getResourceAttr(resource, object, String.format("%s_key", resourcetype));
        } else {
            uploadtype = null;
            resourcekey = "";
        }
        System.out.println(id + " " + resourcekey);

        ServiceUtils.operateQiniuResourceAsync(resourcefile, resourcekey, uploadtype, ServiceUtils.QiniuOperationType.REUPLOAD);
        return "success";
    }
}