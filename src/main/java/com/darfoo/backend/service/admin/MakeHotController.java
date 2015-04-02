package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.cota.enums.ResourceHot;
import com.darfoo.backend.service.cota.TypeClassMapping;
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
 * Created by zjh on 15-4-1.
 */

@Controller
public class MakeHotController {
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "/admin/{type}/changehot", method = RequestMethod.GET)
    public String prepareChangeDanceGroupHot(@PathVariable String type, ModelMap modelMap) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("hot", ResourceHot.NOTHOT);
        List nothotDanceGroups = commonDao.getResourcesByFields(resource, conditions);
        conditions.put("hot", ResourceHot.ISHOT);
        List hotDanceGroups = commonDao.getResourcesByFields(resource, conditions);
        modelMap.addAttribute("nothotresources", nothotDanceGroups);
        modelMap.addAttribute("hotresources", hotDanceGroups);
        return "resource/changeresourcehot";
    }

    @RequestMapping(value = "/admin/{type}/changehot/{hot}", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer changeDanceGroupHot(@PathVariable String type, @PathVariable ResourceHot hot, HttpServletRequest request) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String ids = request.getParameter("ids");
        String[] idArray = ids.split(",");

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("hot", hot);

        for (String id : idArray) {
            Integer resourceid = Integer.parseInt(id);
            commonDao.updateResourceFieldsById(resource, resourceid, conditions);
        }
        return 200;
    }
}
