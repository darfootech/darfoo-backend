package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zjh on 14-12-4.
 */


@Controller
public class DeleteController {
    @Autowired
    VideoDao videoDao;
    @Autowired
    TutorialDao educationDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "/admin/video/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteVideo(HttpServletRequest request, HttpSession session){
        Integer vid = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(commonDao.deleteResourceById(Video.class, vid));
        if (status.equals("DELETE_SUCCESS")){
            return 200+"";
        }else{
            return 505+"";
        }
    }

    @RequestMapping(value = "/admin/tutorial/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteTutorial(HttpServletRequest request, HttpSession session){
        Integer vid = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(educationDao.deleteEducationById(vid));
        System.out.println(status);
        if (status.equals("DELETE_SUCCESS")){
            return 200+"";
        }else{
            return 505+"";
        }
    }

    @RequestMapping(value = "/admin/music/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteMusic(HttpServletRequest request, HttpSession session){
        Integer id = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(musicDao.deleteMusicById(id));
        System.out.println(status);
        if (status.equals("DELETE_SUCCESS")){
            return 200+"";
        }else{
            return 505+"";
        }
    }

    @RequestMapping(value = "/admin/author/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteAuthor(HttpServletRequest request, HttpSession session){
        Integer id = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(authorDao.deleteAuthorById(id));
        System.out.println(status);
        if (status.equals("DELETE_SUCCESS")){
            return 200+"";
        }else{
            return 505+"";
        }
    }
}
