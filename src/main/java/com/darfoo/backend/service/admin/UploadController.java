package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Advertise;
import com.darfoo.backend.model.cota.annotations.ModelInsert;
import com.darfoo.backend.model.cota.annotations.ModelUpload;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.model.cota.enums.OperaVideoType;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
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
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    @Autowired
    CommonDao commonDao;

    public int commonInsertResource(Class resource, HttpServletRequest request, HttpSession session) {
        HashMap<String, String> insertcontents = new HashMap<String, String>();

        for (Field field : resource.getDeclaredFields()) {
            if (field.isAnnotationPresent(ModelInsert.class)) {
                String insertkey = field.getName().toLowerCase();
                insertcontents.put(insertkey, request.getParameter(insertkey));
            }
        }

        if (resource == DanceVideo.class) {
            String[] categories = request.getParameterValues("categories");
            if (categories == null) {
                insertcontents.put("category", "");
            } else {
                for (String category : categories) {
                    insertcontents.put(String.format("category%s", category), DanceVideoCates.danceVideoCategoryMap.get(category));
                }
            }
        }

        if (resource == DanceVideo.class || resource == DanceGroup.class || resource == OperaVideo.class) {
            insertcontents.put("type", request.getParameter("innertype").toLowerCase());
        }

        if (resource == DanceVideo.class || resource == DanceGroup.class || resource == OperaVideo.class || resource == OperaSeries.class || resource == Advertise.class) {
            String imagekey = String.format("%s-imagekey-%s.%s", resource.getSimpleName().toLowerCase(), System.currentTimeMillis(), request.getParameter("imagetype"));
            insertcontents.put("imagekey", imagekey);
            session.setAttribute("imagekey", imagekey);
        }

        if (resource == DanceMusic.class) {
            insertcontents.put("category", request.getParameter("musicletter").toUpperCase());
        }

        HashMap<String, Integer> result = commonDao.insertResource(resource, insertcontents);
        int statuscode = result.get("statuscode");
        int insertid = result.get("insertid");

        System.out.println("status code is -> " + statuscode);

        if (resource == DanceVideo.class || resource == OperaVideo.class) {
            String videokey = String.format("%s-%s-%d.%s", insertcontents.get("title"), resource.getSimpleName().toLowerCase(), insertid, insertcontents.get("videotype"));
            session.setAttribute("videokey", videokey);
        }

        if (resource == DanceMusic.class) {
            String musickey = String.format("%s-%s-%d.%s", insertcontents.get("title"), resource.getSimpleName().toLowerCase(), insertid, "mp3");
            session.setAttribute("musickey", musickey);
        }

        session.setAttribute("insertid", insertid);
        return statuscode;
    }

    public String commonUploadResource(Class resource, HashMap<String, CommonsMultipartFile> uploadresources, HttpSession session) {
        for (Field field : resource.getDeclaredFields()) {
            if (field.isAnnotationPresent(ModelUpload.class)) {
                field.setAccessible(true);
                Annotation annotation = field.getAnnotation(ModelUpload.class);
                ModelUpload modelUpload = (ModelUpload) annotation;

                final CommonsMultipartFile file = uploadresources.get(field.getName());
                final String key = session.getAttribute(field.getName()).toString();
                final ModelUploadEnum type = modelUpload.type();

                ServiceUtils.operateQiniuResourceAsync(file, key, type, ServiceUtils.QiniuOperationType.UPLOAD);
            }
        }
        return "success";
    }

    @RequestMapping(value = "/resources/new/{type}", method = RequestMethod.GET)
    public String uploadResourceWithType(@PathVariable String type, ModelMap modelMap) {
        if (type.equals("dancegroup")) {
            modelMap.put("typenames", TypeClassMapping.danceGroupTypeNameMap);
        }
        if (type.equals("dancevideo")) {
            modelMap.put("typenames", TypeClassMapping.danceVideoTypeNameMap);
        }
        if (type.equals("operavideo")) {
            modelMap.put("typenames", TypeClassMapping.operaVideoTypeNameMap);
        }
        if (type.equals("dancemusic")) {
            return "upload/uploaddancemusic";
        }
        if (type.equals("operaseries")) {
            return "upload/uploadoperaseries";
        }
        if (type.equals("advertise")) {
            return "upload/uploadadvertise";
        }
        modelMap.put("operation", "upload");
        modelMap.put("type", type);
        return "resource/resourcetype";
    }

    @RequestMapping(value = "/resources/new/{type}/{innertype}", method = RequestMethod.GET)
    public String uploadResource(@PathVariable String type, @PathVariable String innertype, ModelMap modelMap) {
        if (type.equals("dancevideo")) {
            DanceVideoType danceVideoType = DanceVideoType.valueOf(innertype.toUpperCase());
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("type", TypeClassMapping.danceVideoTypeDanceGroupTypeMap.get(danceVideoType));
            modelMap.addAttribute("authors", commonDao.getResourcesByFields(DanceGroup.class, conditions));
        } else if (type.equals("operavideo")) {
            OperaVideoType operaVideoType = OperaVideoType.valueOf(innertype.toUpperCase());
            if (operaVideoType == OperaVideoType.SERIES) {
                modelMap.addAttribute("serieses", commonDao.getAllResource(OperaSeries.class));
            }
        }

        modelMap.addAttribute("type", type);
        modelMap.addAttribute("innertype", innertype);

        return String.format("upload/upload%s", type);
    }

    @RequestMapping(value = "/resources/{type}/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer createResource(@PathVariable String type, HttpServletRequest request, HttpSession session) {
        return commonInsertResource(TypeClassMapping.typeClassMap.get(type), request, session);
    }

    //给自动上传工具提供resourcekey
    @RequestMapping(value = "/resources/{type}/autocreate", method = RequestMethod.POST)
    public
    @ResponseBody
    HashMap<String, String> autoCreateResource(@PathVariable String type, HttpServletRequest request, HttpSession session) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        int status = commonInsertResource(resource, request, session);
        HashMap<String, String> result = new HashMap<String, String>();
        if (status == 200) {
            int insertid = (Integer) session.getAttribute("insertid");
            Object object = commonDao.getResourceById(resource, insertid);
            if (type.equals("dancevideo") || type.equals("operavideo")) {
                String videokey = (String) commonDao.getResourceAttr(resource, object, "video_key");
                String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
                System.out.println("videokey ->" + videokey);
                System.out.println("imagekey ->" + imagekey);
                result.put("videokey", videokey);
                result.put("imagekey", imagekey);
            }

            if (type.equals("dancemusic")) {
                String musickey = (String) commonDao.getResourceAttr(resource, object, "music_key");
                result.put("musickey", musickey);
            }

            if (type.equals("dancegroup")) {
                String imagekey = ((Image) commonDao.getResourceAttr(resource, object, "image")).getImage_key();
                result.put("imagekey", imagekey);
            }

            result.put("error", "nop");
        } else {
            result.put("error", "yep");
            result.put("status", String.format("%d", status));
        }

        System.out.println("title -> " + request.getParameter("title"));
        System.out.println("insertstatus ->" + status);
        return result;
    }

    @RequestMapping(value = "/resources/{type}/resource/new", method = RequestMethod.GET)
    public String uploadMediaResource(@PathVariable String type) {
        return String.format("upload/upload%sresource", type);
    }

    @RequestMapping(value = "/resources/{type}/videoimage/create", method = RequestMethod.POST)
    public String uploadVideoImageResource(@PathVariable String type, @RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("videokey", videoresource);
        uploadresources.put("imagekey", imageresource);
        return commonUploadResource(TypeClassMapping.typeClassMap.get(type), uploadresources, session);
    }

    @RequestMapping(value = "/resources/{type}/music/create", method = RequestMethod.POST)
    public String uploadMusicResource(@PathVariable String type, @RequestParam("musicresource") CommonsMultipartFile musicresource, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("musickey", musicresource);
        return commonUploadResource(TypeClassMapping.typeClassMap.get(type), uploadresources, session);
    }

    @RequestMapping(value = "/resources/{type}/image/create", method = RequestMethod.POST)
    public String uploadImageResource(@PathVariable String type, @RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("imagekey", imageresource);
        return commonUploadResource(TypeClassMapping.typeClassMap.get(type), uploadresources, session);
    }
}