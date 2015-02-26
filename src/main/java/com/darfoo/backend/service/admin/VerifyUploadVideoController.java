package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.resource.ImageDao;
import com.darfoo.backend.dao.upload.UploadNoAuthVideoDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zjh on 15-1-17.
 */

@Controller
@RequestMapping("/admin/verifyvideo")
public class VerifyUploadVideoController {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    UploadNoAuthVideoDao uploadNoAuthVideoDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    AccompanyDao accompanyDao;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String allUnVerifyVideos(ModelMap modelMap) {
        List<UploadNoAuthVideo> videos = uploadNoAuthVideoDao.getAllUnVerifyVideos();
        modelMap.addAttribute("allvideos", videos);
        return "verifyallvideo";
    }

    /*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String singleUnVerifyVideos(@PathVariable Integer id, ModelMap modelMap, HttpSession session) {
        UploadNoAuthVideo video = uploadNoAuthVideoDao.getUploadVideoById(id);
        session.setAttribute("uploadvideoid", id);
        session.setAttribute("uploadmacaddr", video.getMac_addr());

        modelMap.addAttribute("videoid", id);
        modelMap.addAttribute("title", video.getVideotitle());
        modelMap.addAttribute("videokey", video.getVideo_key());
        modelMap.addAttribute("imagekey", video.getImage_key());
        modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw(video.getImage_key()));
        //modelMap.addAttribute("imageurl", qiniuUtils.getQiniuResourceUrlRaw("心里藏着你-348.mp4@@recommendvideo.png"));
        modelMap.addAttribute("videourl", qiniuUtils.getQiniuResourceUrlRaw(video.getVideo_key()));
        //modelMap.addAttribute("videourl", qiniuUtils.getQiniuResourceUrlRaw("心里藏着你-348.mp4"));
        modelMap.addAttribute("videotype", video.getVideotype());
        return "verifysinglevideo";
    }

    public HashMap<String, Integer> insertSingleVideo(String videokey, String videotitle, String videotype, String authorname, String imagekey, String videospeed, String videodifficult, String videostyle, String videoletter) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoletter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        if (imagekey.equals("")) {
            resultMap.put("statuscode", 508);
            resultMap.put("insertid", -1);
            return resultMap;
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
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        Author targetAuthor = (Author) commonDao.getResourceByTitleOrName(Author.class, authorname, "name");
        if (targetAuthor != null) {
            System.out.println(targetAuthor.getName());
        } else {
            targetAuthor = new Author();
            targetAuthor.setName(authorname);
            targetAuthor.setDescription("userdescription");
            targetAuthor.setImage(null);
            authorDao.insertAuthor(targetAuthor);
        }

        Video video = new Video();
        video.setAuthor(targetAuthor);
        Image img = new Image();
        img.setImage_key(imagekey);
        video.setImage(img);
        VideoCategory speed = new VideoCategory();
        VideoCategory difficult = new VideoCategory();
        VideoCategory style = new VideoCategory();
        VideoCategory letter = new VideoCategory();
        speed.setTitle(videospeed);
        difficult.setTitle(videodifficult);
        style.setTitle(videostyle);
        letter.setTitle(videoletter);
        Set<VideoCategory> s_vCategory = video.getCategories();
        s_vCategory.add(speed);
        s_vCategory.add(difficult);
        s_vCategory.add(style);
        s_vCategory.add(letter);
        video.setTitle(videotitle);
        video.setVideo_key(videokey);
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = videoDao.insertSingleVideo(video);
        if (insertStatus == -1) {
            System.out.println("插入视频失败");
        } else {
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        return resultMap;
    }*/

    /*@RequestMapping(value = "/verify", method = RequestMethod.POST)
    public
    @ResponseBody
    String verifyUploadVideo(HttpServletRequest request, HttpSession session) {
        String videoTitle = request.getParameter("title");
        String videoKey = request.getParameter("videokey");
        String videoType = request.getParameter("videotype");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        String videoLetter = request.getParameter("videoletter").toUpperCase();
        String connectmusic = request.getParameter("connectmusic");
        System.out.println("connectmusic -> " + connectmusic);

        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoKey + " " + videoTitle + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + videoLetter + " " + update_timestamp);

        String authorname = "user-" + (String) session.getAttribute("uploadmacaddr");

        HashMap<String, Integer> resultMap = this.insertSingleVideo(videoKey, videoTitle, videoType, authorname, imagekey, videoSpeed, videoDifficult, videoStyle, videoLetter);
        int statusCode = resultMap.get("statuscode");
        System.out.println("status code is: " + statusCode);
        if (statusCode != 200) {
            return statusCode + "";
        } else {
            int insertid = resultMap.get("insertid");
            int uploadvideoid = (Integer) session.getAttribute("uploadvideoid");
            uploadNoAuthVideoDao.updateRealVideoid(uploadvideoid, insertid);

            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(Video.class, insertid, mid);
            }

            return statusCode + "";
        }
    }*/

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    String deleteVideo(HttpServletRequest request) {
        Integer vid = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(uploadNoAuthVideoDao.deleteVideoById(vid));
        if (status.equals("DELETE_SUCCESS")) {
            return 200 + "";
        } else {
            return 505 + "";
        }
    }
}
