package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zjh on 14-12-4.
 */

@Controller
public class DeleteController {
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "admin/{type}/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer deleteResource(@PathVariable String type, HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String status = CRUDEvent.getResponse(commonDao.deleteResourceById(TypeClassMapping.typeClassMap.get(type), id));
        if (status.equals("DELETE_SUCCESS")) {
            return 200;
        } else {
            return 505;
        }
    }
}