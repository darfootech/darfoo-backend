package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.UploadNoAuthVideoDao;
import com.darfoo.backend.model.UploadNoAuthVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String allUnVerifyVideos(ModelMap modelMap){
        List<UploadNoAuthVideo> videos = uploadNoAuthVideoDao.getAllUnVerifyVideos();
        modelMap.addAttribute("allvideos", videos);
        return "verifyallvideo";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String singleUnVerifyVideos(@PathVariable Integer id, ModelMap modelMap){
        UploadNoAuthVideo video = uploadNoAuthVideoDao.getUploadVideoById(id);
        modelMap.addAttribute("title", video.getVideotitle());
        modelMap.addAttribute("authors", authorDao.getAllAuthor());
        return "verifysinglevideo";
    }
}
