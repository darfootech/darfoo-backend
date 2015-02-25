package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by zjh on 15-1-4.
 */

/*管理人员可以选择要推荐的欣赏和教学视频 并且可以上传指定要和每一个推荐视频关联的图片*/

@Controller
public class RecommendController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    QiniuUtils qiniuUtils;

    @RequestMapping(value = "/admin/recommend/{type}", method = RequestMethod.GET)
    public String recommendResource(@PathVariable String type, ModelMap modelMap) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List unRecommendResources = recommendDao.getUnRecommendResources(resource);
        List recommendResources = recommendDao.getRecommendResources(resource);
        modelMap.addAttribute("unrecommendresources", unRecommendResources);
        modelMap.addAttribute("recommendresources", recommendResources);
        modelMap.addAttribute("type", resource.getSimpleName().toLowerCase());
        return "recommend/recommendresource";
    }

    @RequestMapping(value = "/admin/recommend/add/{type}", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Integer doRecommendResources(@PathVariable String type, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        System.out.println(ids);
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            int status = recommendDao.doRecommendResource(TypeClassMapping.typeClassMap.get(type), Integer.parseInt(idArray[i]));
            if (status != CRUDEvent.UPDATE_SUCCESS) {
                return 500;
            }
        }
        return 200;
    }

    @RequestMapping(value = "/admin/recommend/del/{type}", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Integer unRecommendResources(@PathVariable String type, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        System.out.println(ids);
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            int status = recommendDao.unRecommendResource(TypeClassMapping.typeClassMap.get(type), Integer.parseInt(idArray[i]));
            if (status != CRUDEvent.UPDATE_SUCCESS) {
                return 500;
            }
        }
        return 200;
    }

    @RequestMapping(value = "/admin/recommend/updateimage/all", method = RequestMethod.GET)
    public String updateRecommendResourcesImage(ModelMap modelMap, HttpSession session) {
        List<Video> recommendVideos = recommendDao.getRecommendResources(Video.class);
        List<Tutorial> recommendTutorials = recommendDao.getRecommendResources(Tutorial.class);
        modelMap.addAttribute("videos", recommendVideos);
        modelMap.addAttribute("tutorials", recommendTutorials);
        return "recommend/updaterecommendimageall";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/{type}/{id}", method = RequestMethod.GET)
    public String updateRecommendResourceImage(@PathVariable String type, @PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        String imagekey = String.format("%s@@recommend%s.png", commonDao.getResourceAttr(resource, object, "video_key"), resource.getSimpleName().toLowerCase());
        session.setAttribute("imagekey", imagekey);
        String imageurl = qiniuUtils.getQiniuResourceUrl(imagekey);
        modelMap.addAttribute("resource", object);
        modelMap.addAttribute("imageurl", imageurl);
        return "recommend/updaterecommendresourceimage";
    }

    @RequestMapping("/admin/recommend/resource/updateimage")
    public String uploadRecommendResourceImage(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        String imagekey = (String) session.getAttribute("imagekey");
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