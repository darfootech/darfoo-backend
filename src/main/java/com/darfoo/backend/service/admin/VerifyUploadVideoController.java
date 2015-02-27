package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.upload.UploadNoAuthVideoDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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
