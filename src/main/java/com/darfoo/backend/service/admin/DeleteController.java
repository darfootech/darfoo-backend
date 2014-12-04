package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.VideoDao;
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

    @RequestMapping(value = "/admin/video/delete", method = RequestMethod.POST)
    public @ResponseBody
    String updateVideo(HttpServletRequest request, HttpSession session){
        Integer vid = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(videoDao.deleteVideoById(vid));
        if (status.equals("DELETE_SUCCESS")){
            return 200+"";
        }else{
            return 505+"";
        }
    }
}
