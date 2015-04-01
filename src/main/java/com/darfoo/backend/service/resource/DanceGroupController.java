package com.darfoo.backend.service.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.DanceVideoDao;
import com.darfoo.backend.model.cota.ResourceHot;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
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
 * Created by zjh on 15-3-20.
 */

@Controller
public class DanceGroupController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    DanceVideoDao danceVideoDao;

    @RequestMapping(value = "/admin/dancegroup/changetype", method = RequestMethod.GET)
    public String prepareChangeDanceGroupType(ModelMap modelMap) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", DanceGroupType.STAR);
        List starDanceGroups = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        conditions.put("type", DanceGroupType.NORMAL);
        List normalDanceGroups = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        modelMap.addAttribute("stardancegroups", starDanceGroups);
        modelMap.addAttribute("normaldancegroups", normalDanceGroups);
        return "author/changedancegrouptype";
    }

    @RequestMapping(value = "/admin/dancegroup/changetype/{type}", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer changeDanceGroupType(@PathVariable DanceGroupType type, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        String[] idArray = ids.split(",");

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", type);

        for (String id : idArray) {
            Integer danceGroupId = Integer.parseInt(id);
            commonDao.updateResourceFieldsById(DanceGroup.class, danceGroupId, conditions);
            danceVideoDao.updateDanceVideoTypeWithDanceGroupId(danceGroupId, TypeClassMapping.danceGroupTypeDanceVideoTypeMap.get(type));
        }
        return 200;
    }
}
