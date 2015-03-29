package com.darfoo.backend.service.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.cota.AuthorType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-3-20.
 */

@Controller
public class AuthorController {
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "/admin/author/changetype", method = RequestMethod.GET)
    public String prepareChangeAuthorType(ModelMap modelMap) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", AuthorType.STAR);
        List starAuthors = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        conditions.put("type", AuthorType.NORMAL);
        List normalAuthors = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        modelMap.addAttribute("starauthors", starAuthors);
        modelMap.addAttribute("normalauthors", normalAuthors);
        return "author/changeauthortype";
    }

    @RequestMapping(value = "/admin/author/changetype/{type}", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer changeAuthorType(@PathVariable AuthorType type, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        String[] idArray = ids.split(",");

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", type);

        for (int i = 0; i < idArray.length; i++) {
            commonDao.updateResourceFieldsById(DanceGroup.class, Integer.parseInt(idArray[i]), conditions);
        }
        return 200;
    }
}
