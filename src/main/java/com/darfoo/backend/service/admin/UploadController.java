package com.darfoo.backend.service.admin;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.cota.ModelInsert;
import com.darfoo.backend.model.cota.ModelUpload;
import com.darfoo.backend.model.cota.ModelUploadEnum;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.ActorSysContainer;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.Callable;

import static akka.dispatch.Futures.future;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    @Autowired
    CommonDao commonDao;

    final ActorSystem system = ActorSysContainer.getInstance().getSystem();
    final ExecutionContext ec = system.dispatcher();

    public int commonInsertResource(Class resource, HttpServletRequest request, HttpSession session) {
        HashMap<String, String> insertcontents = new HashMap<String, String>();

        for (Field field : resource.getDeclaredFields()) {
            if (field.isAnnotationPresent(ModelInsert.class)) {
                String insertkey = field.getName().toLowerCase();
                insertcontents.put(insertkey, request.getParameter(insertkey));
            }
        }

        if (resource == Video.class) {
            insertcontents.put("category1", request.getParameter("videospeed"));
            insertcontents.put("category2", request.getParameter("videodifficult"));
            insertcontents.put("category3", request.getParameter("videostyle"));
            insertcontents.put("category4", request.getParameter("videoletter").toUpperCase());
        }

        if (resource == Tutorial.class) {
            insertcontents.put("category1", request.getParameter("videospeed"));
            insertcontents.put("category2", request.getParameter("videodifficult"));
            insertcontents.put("category3", request.getParameter("videostyle"));
        }

        if (resource == Music.class) {
            insertcontents.put("category1", request.getParameter("musicbeat"));
            insertcontents.put("category2", request.getParameter("musicstyle"));
            insertcontents.put("category3", request.getParameter("musicletter").toUpperCase());
        }

        HashMap<String, Integer> result = commonDao.insertResource(resource, insertcontents);
        int statuscode = result.get("statuscode");
        int insertid = result.get("insertid");

        System.out.println("status code is -> " + statuscode);

        if (resource == Video.class || resource == Tutorial.class) {
            session.setAttribute("videokey", insertcontents.get("title") + "-" + resource.getSimpleName().toLowerCase() + "-" + insertid + "." + insertcontents.get("videotype"));
            session.setAttribute("imagekey", insertcontents.get("imagekey"));
        }

        if (resource == Music.class) {
            session.setAttribute("musickey", insertcontents.get("title") + "-" + insertid + ".mp3");
        }

        if (resource == Author.class) {
            session.setAttribute("imagekey", insertcontents.get("imagekey"));
        }

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

                Future<String> future = future(new Callable<String>() {
                    public String call() {
                        String status = ServiceUtils.uploadQiniuResource(file, key, type);
                        return status;
                    }
                }, system.dispatcher());

                future.onComplete(new OnComplete<String>() {
                    public void onComplete(Throwable failure, String status) {
                        if (failure != null && !status.equals("200")) {
                            System.out.println("upload file failed");
                        } else {
                            System.out.println("upload file success");
                        }
                    }
                }, ec);
            }
        }
        return "success";
    }

    @RequestMapping(value = "/resources/{type}/new", method = RequestMethod.GET)
    public String uploadResource(@PathVariable String type, ModelMap modelMap) {
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        return String.format("upload/upload%s", type);
    }

    @RequestMapping(value = "/resources/{type}/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer createResource(@PathVariable String type, HttpServletRequest request, HttpSession session) {
        return commonInsertResource(TypeClassMapping.typeClassMap.get(type), request, session);
    }

    @RequestMapping(value = "/resources/{type}resource/new", method = RequestMethod.GET)
    public String uploadMediaResource(@PathVariable String type) {
        return String.format("upload/upload%sresource", type);
    }

    @RequestMapping(value = "/resources/{type}resource/create")
    public String createMediaResource(@RequestParam("videoresource") CommonsMultipartFile videoresource, @RequestParam("imageresource") CommonsMultipartFile imageresource, @PathVariable String type, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("videokey", videoresource);
        uploadresources.put("imagekey", imageresource);
        return commonUploadResource(TypeClassMapping.typeClassMap.get(type), uploadresources, session);
    }

    @RequestMapping("/resources/musicresource/create")
    public String createMusicResourceNoPic(@RequestParam("musicresource") CommonsMultipartFile musicresource, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("musickey", musicresource);
        return commonUploadResource(Music.class, uploadresources, session);
    }

    @RequestMapping("/resources/authorresource/create")
    public String createAuthorResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        HashMap<String, CommonsMultipartFile> uploadresources = new HashMap<String, CommonsMultipartFile>();
        uploadresources.put("imagekey", imageresource);
        return commonUploadResource(Author.class, uploadresources, session);
    }
}