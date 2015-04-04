package com.darfoo.backend.service.admin;

import com.darfoo.backend.dao.cota.CommonDao;
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

//变化某一个字段的值 比如是否是热门资源 是否是高优先级的资源
@Controller
public class ToggleFieldValueController {
    @Autowired
    CommonDao commonDao;

    @RequestMapping(value = "/admin/{type}/change/{field}", method = RequestMethod.GET)
    public String prepareToggleFieldValue(@PathVariable String type, @PathVariable String field, ModelMap modelMap) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        Enum negativeValue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("NOT%s", field.toUpperCase()));
        Enum positiveValue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("IS%s", field.toUpperCase()));
        conditions.put(field, negativeValue);
        List negativeResources = commonDao.getResourcesByFields(resource, conditions);
        conditions.put(field, positiveValue);
        List positiveResources = commonDao.getResourcesByFields(resource, conditions);

        String literaltype = TypeClassMapping.typeNameLiteralMap.get(type);
        String literalfield = TypeClassMapping.fieldNameLiteralMap.get(field);

        //貌似可以直接把pathvariable传到jsp中 比如这里的type和field 可以直接在jsp中访问
        modelMap.addAttribute("negativeresources", negativeResources);
        modelMap.addAttribute("positiveresources", positiveResources);
        modelMap.addAttribute("negativemessage", String.format("选中下面的%s然后点击这里变为%s%s", literaltype, literalfield, literaltype));
        modelMap.addAttribute("positivemessage", String.format("选中下面的%s然后点击这里变为非%s%s", literaltype, literalfield, literaltype));
        modelMap.addAttribute("negativealert", String.format("还没有选中要变为%s%s的%s", literalfield, literaltype, literaltype));
        modelMap.addAttribute("positivealert", String.format("还没有选中要变为非%s%s的%s", literalfield, literaltype, literaltype));
        return "resource/toggleresourcefield";
    }

    @RequestMapping(value = "/admin/{type}/change/{field}/{value}", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer toggleFieldValue(@PathVariable String type, @PathVariable String field, @PathVariable String value, HttpServletRequest request) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String ids = request.getParameter("ids");
        String[] idArray = ids.split(",");

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        Enum targetValue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), value);
        conditions.put(field, targetValue);

        for (String id : idArray) {
            Integer resourceid = Integer.parseInt(id);
            commonDao.updateResourceFieldsById(resource, resourceid, conditions);
        }
        return 200;
    }
}
