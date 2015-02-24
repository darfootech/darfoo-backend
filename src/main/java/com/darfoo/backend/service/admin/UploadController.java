package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.*;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.cota.ModelInsert;
import com.darfoo.backend.model.cota.ModelUpload;
import com.darfoo.backend.model.cota.ModelUploadEnum;
import com.darfoo.backend.model.resource.*;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public int insertSingleAuthor(String authorname, String description, String imagekey) {
        if (authorDao.isExistAuthor(authorname)) {
            System.out.println("作者已存在");
            return 501;
        } else {
            System.out.println("无该author记录，可以创建");
        }

        if (imagekey.equals("")) {
            return 508;
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", imagekey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return 503;
        }

        Author author = new Author();
        author.setName(authorname);
        author.setDescription(description);
        author.setImage(image);
        authorDao.insertAuthor(author);

        return 200;
    }

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
            session.setAttribute("videokey", insertcontents.get("title") + "-" + insertid + "." + insertcontents.get("videotype"));
            session.setAttribute("imagekey", insertcontents.get("imagekey"));
        }

        if (resource == Music.class) {
            session.setAttribute("musickey", insertcontents.get("title") + "-" + insertid + ".mp3");
        }

        return statuscode;
    }

    public String commonUploadResource(Class resource, HashMap<String, CommonsMultipartFile> uploadresources, HttpSession session) {
        for (Field field : resource.getDeclaredFields()) {
            if (field.isAnnotationPresent(ModelUpload.class)) {
                field.setAccessible(true);
                Annotation annotation = field.getAnnotation(ModelUpload.class);
                ModelUpload modelUpload = (ModelUpload) annotation;

                CommonsMultipartFile file = uploadresources.get(field.getName());
                String key = session.getAttribute(field.getName()).toString();

                try {
                    if (modelUpload.type() == ModelUploadEnum.SMALL) {
                        String status = ServiceUtils.uploadSmallResource(file, key);
                        if (!status.equals("200")) {
                            return "fail";
                        }
                    } else if (modelUpload.type() == ModelUploadEnum.LARGE) {
                        String status = ServiceUtils.uploadLargeResource(file, key);
                        if (!status.equals("200")) {
                            return "fail";
                        }
                    } else {
                        System.out.println("wired");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "fail";
                }
            }
        }

        return "success";
    }

    @RequestMapping(value = "/resources/{type}/new", method = RequestMethod.GET)
    public String uploadResource(@PathVariable String type, ModelMap modelMap, HttpSession session) {
        session.setAttribute("resource", type);
        modelMap.addAttribute("resource", type);
        modelMap.addAttribute("authors", commonDao.getAllResource(Author.class));
        return "upload" + type;
    }

    @RequestMapping(value = "/resources/{type}/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer createResource(@PathVariable String type, HttpServletRequest request, HttpSession session) {
        return commonInsertResource(TypeClassMapping.typeClassMap.get(type), request, session);
    }

    @RequestMapping(value = "/resources/{type}resource/new", method = RequestMethod.GET)
    public String uploadMediaResource(@PathVariable String type) {
        return String.format("upload%sresource", type);
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

    @RequestMapping(value = "/resources/author/create", method = RequestMethod.POST)
    public
    @ResponseBody
    String createAuthor(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        System.out.println("requests: " + name + " " + description);

        session.setAttribute("authorImage", imagekey);

        int statusCode = this.insertSingleAuthor(name, description, imagekey);
        return statusCode + "";
    }

    @RequestMapping("/resources/authorresource/create")
    public String createAuthorResource(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        String imagekey = (String) session.getAttribute("authorImage");
        System.out.println("imagekey in session: " + imagekey);

        String imageResourceName = imageresource.getOriginalFilename();

        System.out.println(imageResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageStatusCode.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
}