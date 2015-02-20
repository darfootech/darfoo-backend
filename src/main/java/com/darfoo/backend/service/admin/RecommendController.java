package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CommonDao;
import com.darfoo.backend.dao.RecommendDao;
import com.darfoo.backend.dao.TutorialDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
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
    VideoDao videoDao;
    @Autowired
    TutorialDao educationDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    QiniuUtils qiniuUtils;


    @RequestMapping(value = "/admin/recommend/video", method = RequestMethod.GET)
    public String recommendVideo(ModelMap modelMap, HttpSession session) {
        List<Video> allVideos = commonDao.getAllResource(Video.class);
        List<Video> recommendVideos = recommendDao.getRecommendResources(Video.class);
        modelMap.addAttribute("allvideos", allVideos);
        modelMap.addAttribute("recommendvideos", recommendVideos);
        return "recommendvideo";
    }

    @RequestMapping(value = "/admin/recommend/addvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String addVideos(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            recommendDao.doRecommendResource(Video.class, Integer.parseInt(videoids[i]));
        }
        return 200 + "";
    }

    @RequestMapping(value = "/admin/recommend/delvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String delVideos(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            recommendDao.unRecommendResource(Video.class, Integer.parseInt(videoids[i]));
        }
        return 200 + "";
    }

    @RequestMapping(value = "/admin/recommend/tutorial", method = RequestMethod.GET)
    public String recommendTutorial(ModelMap modelMap, HttpSession session) {
        List<Tutorial> allTutorials = commonDao.getAllResource(Tutorial.class);
        List<Tutorial> recommendTutorials = educationDao.getRecommendTutorials();
        modelMap.addAttribute("alltutorials", allTutorials);
        modelMap.addAttribute("recommendtutorials", recommendTutorials);
        return "recommendtutorial";
    }

    @RequestMapping(value = "/admin/recommend/addtutorials", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String addTutorials(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            educationDao.doRecommendTutorial(Integer.parseInt(videoids[i]));
        }
        return 200 + "";
    }

    @RequestMapping(value = "/admin/recommend/deltutorials", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    String delTutorials(HttpServletRequest request, HttpSession session) {
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        for (int i = 0; i < videoids.length; i++) {
            educationDao.unRecommendTutorial(Integer.parseInt(videoids[i]));
        }
        return 200 + "";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/all", method = RequestMethod.GET)
    public String updateRecommendImage(ModelMap modelMap, HttpSession session) {
        List<Video> recommendVideos = recommendDao.getRecommendResources(Video.class);
        List<Tutorial> recommendTutorials = educationDao.getRecommendTutorials();
        modelMap.addAttribute("videos", recommendVideos);
        modelMap.addAttribute("tutorials", recommendTutorials);
        return "updaterecommendimageall";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/video/{id}", method = RequestMethod.GET)
    public String updateRecommendImageVideo(@PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        Video video = (Video) commonDao.getResourceById(Video.class, id);
        String imagekey = video.getVideo_key() + "@@recommendvideo.png";
        session.setAttribute("imagekey", imagekey);
        String imageurl = qiniuUtils.getQiniuResourceUrl(imagekey);
        modelMap.addAttribute("video", video);
        modelMap.addAttribute("imageurl", imageurl);
        return "updaterecommendimagevideo";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/tutorial/{id}", method = RequestMethod.GET)
    public String updateRecommendImageTutorial(@PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, id);
        String imagekey = tutorial.getVideo_key() + "@@recommendtutorial.png";
        session.setAttribute("imagekey", imagekey);
        String imageurl = qiniuUtils.getQiniuResourceUrl(imagekey);
        modelMap.addAttribute("video", tutorial);
        modelMap.addAttribute("imageurl", imageurl);
        return "updaterecommendimagetutorial";
    }

    @RequestMapping("/admin/recommend/video/updateimage")
    public String updateRecommendVideoPic(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String imagekey = (String) session.getAttribute("imagekey");

        String videoResourceName = imageresource.getOriginalFilename();

        System.out.println("imageresource -> " + videoResourceName);

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

    @RequestMapping("/admin/recommend/tutorial/updateimage")
    public String updateRecommendTutorialPic(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session) {
        //upload
        String imagekey = (String) session.getAttribute("imagekey");

        String videoResourceName = imageresource.getOriginalFilename();

        System.out.println("imageresource -> " + videoResourceName);

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
