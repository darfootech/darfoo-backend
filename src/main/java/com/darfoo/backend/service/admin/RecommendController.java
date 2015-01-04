package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.utils.FileUtils;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    public void setRecommendList(List<Integer> videoids, String flag){
        String filename = "recommend" + flag + ".data";
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
        String filename = "recommend" + flag + ".data";
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
        String filename = "recommend" + flag + ".data";
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
        List<Integer> videoids = getRecommendList("video");
        List<Video> recommendVideos = new ArrayList<Video>();
        for (Integer vid : videoids){
            recommendVideos.add(videoDao.getVideoByVideoId(vid));
        }
        modelMap.addAttribute("allvideos", allVideos);
        modelMap.addAttribute("recommendvideos", recommendVideos);
        return "recommendvideo";
    }

    @RequestMapping(value = "/admin/recommend/addvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody
    String addConnections(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            videoidList.add(Integer.parseInt(videoids[i]));
        }
        setRecommendList(videoidList, "video");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/delvideos", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public @ResponseBody String delConnections(HttpServletRequest request, HttpSession session){
        String idss = request.getParameter("vids");
        System.out.println(idss);
        String[] videoids = idss.split(",");
        List<Integer> videoidList = new ArrayList<Integer>();
        for (int i=0; i<videoids.length; i++){
            videoidList.add(Integer.parseInt(videoids[i]));
        }
        updateRecommendList(videoidList, "video");
        return 200+"";
    }

    @RequestMapping(value = "/admin/recommend/tutorial", method = RequestMethod.GET)
    public String recommendTutorial(ModelMap modelMap, HttpSession session){
        List<Education> allTutorials = educationDao.getAllEducation();
        List<Education> recommendTutorials = new ArrayList<Education>();
        List<Integer> tutorialids = getRecommendList("tutorial");
        for (Integer tid : tutorialids){
            recommendTutorials.add(educationDao.getEducationVideoById(tid));
        }
        modelMap.addAttribute("alltutorials", allTutorials);
        modelMap.addAttribute("recommendtutorials", recommendTutorials);
        return "recommendtutorial";
    }

}
