package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.utils.FileUtils;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.RecommendManager;
import com.darfoo.backend.utils.ServiceUtils;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
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
    EducationDao educationDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    public void setRecommendList(List<Integer> videoids, String flag){
        String filename = RecommendManager.basepath + "recommend" + flag + ".data";
        FileUtils.createFile(filename);

        File file = new File(filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Integer videoid : videoids){
                stringBuilder.append(videoid.toString()).append("\n");
            }

            byte[] bytes = stringBuilder.toString().getBytes();
            outputStream.write(bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRecommendList(String flag){
        String filename = RecommendManager.basepath + "recommend" + flag + ".data";
        FileUtils.createFile(filename);
        List<Integer> videoList = new ArrayList<Integer>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                videoList.add(Integer.parseInt(line));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videoList;
    }

    public void updateRecommendList(List<Integer> videoids, String flag){
        String filename = RecommendManager.basepath + "recommend" + flag + ".data";
        List<Integer> videoList = new ArrayList<Integer>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                videoList.add(Integer.parseInt(line));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (videoList.size() == 0){
            return;
        }else{
            FileUtils.createFile(filename);

            File file = new File(filename);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                StringBuilder stringBuilder = new StringBuilder();

                for (Integer videoid : videoList){
                    if (!videoids.contains(videoid)){
                        stringBuilder.append(videoid.toString()).append("\n");
                    }
                }

                byte[] bytes = stringBuilder.toString().getBytes();
                outputStream.write(bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/admin/recommend/video", method = RequestMethod.GET)
    public String recommendVideo(ModelMap modelMap, HttpSession session){
        List<Video> allVideos = videoDao.getAllVideo();
        /*List<Integer> videoids = getRecommendList("video");
        List<Video> recommendVideos = new ArrayList<Video>();
        for (Integer vid : videoids){
            recommendVideos.add(videoDao.getVideoByVideoId(vid));
        }*/
        List<Video> recommendVideos = videoDao.getRecommendVideos();
        modelMap.addAttribute("allvideos", allVideos);
        modelMap.addAttribute("recommendvideos", recommendVideos);
        return "recommendvideo";
    }

    @RequestMapping(value = "/admin/recommend/addvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody
    String addVideos(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        //List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            //videoidList.add(Integer.parseInt(videoids[i]));
            videoDao.doRecommendVideo(Integer.parseInt(videoids[i]));
        }
        //setRecommendList(videoidList, "video");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/delvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody String delVideos(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        //List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            //videoidList.add(Integer.parseInt(videoids[i]));
            videoDao.unRecommendVideo(Integer.parseInt(videoids[i]));
        }
        //updateRecommendList(videoidList, "video");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/tutorial", method = RequestMethod.GET)
    public String recommendTutorial(ModelMap modelMap, HttpSession session){
        List<Education> allTutorials = educationDao.getAllEducation();
        /*List<Education> recommendTutorials = new ArrayList<Education>();
        List<Integer> tutorialids = getRecommendList("tutorial");
        for (Integer tid : tutorialids){
            recommendTutorials.add(educationDao.getEducationVideoById(tid));
        }*/
        List<Education> recommendTutorials = educationDao.getRecommendTutorials();
        modelMap.addAttribute("alltutorials", allTutorials);
        modelMap.addAttribute("recommendtutorials", recommendTutorials);
        return "recommendtutorial";
    }

    @RequestMapping(value = "/admin/recommend/addtutorials", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody
    String addTutorials(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        //List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            //videoidList.add(Integer.parseInt(videoids[i]));
            educationDao.doRecommendTutorial(Integer.parseInt(videoids[i]));
        }
        //setRecommendList(videoidList, "tutorial");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/deltutorials", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody String delTutorials(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        //List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            //videoidList.add(Integer.parseInt(videoids[i]));
            educationDao.unRecommendTutorial(Integer.parseInt(videoids[i]));
        }
        //updateRecommendList(videoidList, "tutorial");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/all", method = RequestMethod.GET)
    public String updateRecommendImage(ModelMap modelMap, HttpSession session){
        //List<Integer> recommendVideoids = getRecommendList("video");
        //List<Video> recommendVideos = new ArrayList<Video>();
        List<Video> recommendVideos = videoDao.getRecommendVideos();
        /*for (Integer id : recommendVideoids){
            recommendVideos.add(videoDao.getVideoByVideoId(id));
        }*/
        //List<Integer> recommendTutorialids = getRecommendList("tutorial");
        //List<Education> recommendTutorials = new ArrayList<Education>();
        /*for (Integer id : recommendTutorialids){
            recommendTutorials.add(educationDao.getEducationVideoById(id));
        }*/
        List<Education> recommendTutorials = educationDao.getRecommendTutorials();
        modelMap.addAttribute("videos", recommendVideos);
        modelMap.addAttribute("tutorials", recommendTutorials);
        return "updaterecommendimageall";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/video/{id}", method = RequestMethod.GET)
    public String updateRecommendImageVideo(@PathVariable Integer id, ModelMap modelMap, HttpSession session){
        Video video = videoDao.getVideoByVideoId(id);
        String imagekey = video.getVideo_key() + "@@recommendvideo.png";
        session.setAttribute("imagekey", imagekey);
        String imageurl = qiniuUtils.getQiniuResourceUrl(imagekey);
        modelMap.addAttribute("video", video);
        modelMap.addAttribute("imageurl", imageurl);
        return "updaterecommendimagevideo";
    }

    @RequestMapping(value = "/admin/recommend/updateimage/tutorial/{id}", method = RequestMethod.GET)
    public String updateRecommendImageTutorial(@PathVariable Integer id, ModelMap modelMap, HttpSession session){
        Education tutorial = educationDao.getEducationVideoById(id);
        String imagekey = tutorial.getVideo_key() + "@@recommendtutorial.png";
        session.setAttribute("imagekey", imagekey);
        String imageurl = qiniuUtils.getQiniuResourceUrl(imagekey);
        modelMap.addAttribute("video", tutorial);
        modelMap.addAttribute("imageurl", imageurl);
        return "updaterecommendimagetutorial";
    }

    @RequestMapping("/admin/recommend/video/updateimage")
    public String updateRecommendVideoPic(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String imagekey = (String)session.getAttribute("imagekey");

        String videoResourceName = imageresource.getOriginalFilename();

        System.out.println("imageresource -> " + videoResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }

    @RequestMapping("/admin/recommend/tutorial/updateimage")
    public String updateRecommendTutorialPic(@RequestParam("imageresource") CommonsMultipartFile imageresource, HttpSession session){
        //upload
        String imagekey = (String)session.getAttribute("imagekey");

        String videoResourceName = imageresource.getOriginalFilename();

        System.out.println("imageresource -> " + videoResourceName);

        String imageStatusCode = "";

        try {
            imageStatusCode = ServiceUtils.uploadSmallResource(imageresource, imagekey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageStatusCode.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
}
