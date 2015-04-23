package com.darfoo.backend.service;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.DanceSearchHistory;
import com.darfoo.backend.model.statistics.OperaSearchHistory;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-3-3.
 */

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    StatisticsDao statisticsDao;
    @Autowired
    CommonDao commonDao;

    String hottestField = "hottest";

    @RequestMapping(value = "{type}/{id}/m/{mac}/h/{host}/u/{uuid}")
    public
    @ResponseBody
    String statisticsClickBehavior(@PathVariable String type, @PathVariable Integer id, @PathVariable String mac, @PathVariable String host, @PathVariable String uuid) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();

        conditions.put("mac", mac);
        conditions.put("hostip", host);
        conditions.put("uuid", uuid);

        if (type.equals("menu") || type.equals("tab")) {
            conditions.put(String.format("%sid", type), id);
            if (type.equals("menu")) {
                conditions.put("title", TypeClassMapping.menuTitleMap.get(id));
            } else {
                conditions.put("title", TypeClassMapping.tabTitleMap.get(id));
            }
            statisticsDao.insertTimeBehavior(TypeClassMapping.clickTimeStatMap.get(type), conditions);
            statisticsDao.insertOrUpdateClickBehavior(TypeClassMapping.clickCountStatMap.get(type), conditions);
        } else {
            conditions.put("type", type);
            conditions.put("resourceid", id);
            Class clazz = TypeClassMapping.typeClassMap.get(type);
            if (type.equals("dancegroup")) {
                //dancegroup doesn't have 'title',only 'name'
                conditions.put("title", commonDao.getResourceAttr(clazz, commonDao.getResourceById(clazz, id), "name"));
            } else {
                conditions.put("title", commonDao.getResourceAttr(clazz, commonDao.getResourceById(clazz, id), "title"));
            }
            statisticsDao.insertTimeBehavior(ResourceClickTime.class, conditions);
            statisticsDao.insertOrUpdateClickBehavior(ResourceClickCount.class, conditions);

            System.out.println(String.format("%s clicked id is: %d\n", type, id));
            System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(TypeClassMapping.typeClassMap.get(type), id, hottestField)));

        }
        return "ok";
    }

    @RequestMapping(value = "search/t/{type}")
    public
    @ResponseBody
    String statisticsSearchHistory(@PathVariable String type, HttpServletRequest request) {
        String content = request.getParameter("search");
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("searchtype", type);
        conditions.put("searchcontent", content);

        if (type.equals("dancevideo") || type.equals("dancemusic")) {
            statisticsDao.insertTimeBehavior(DanceSearchHistory.class, conditions);
        }

        if (type.equals("operavideo")) {
            statisticsDao.insertTimeBehavior(OperaSearchHistory.class, conditions);
        }
        return "ok";
    }

    @RequestMapping(value = "crashlog")
    public
    @ResponseBody
    String statisticsCrashLog(HttpServletRequest request) {
        String loginfo = request.getParameter("loginfo");
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("log", loginfo);

        statisticsDao.insertTimeBehavior(CrashLog.class, conditions);
        return "ok";
    }

    @RequestMapping(value = "/admin/{type}/hotsearch", method = RequestMethod.GET)
    public String prepareRecommendHotSearch(@PathVariable String type, ModelMap modelMap) {
        List negativeResources = statisticsDao.getSearchKeyWordsOrderByTypeByHot(type);
        List positiveResources = statisticsDao.getHotSearchKeyWordsByType(type);

        modelMap.addAttribute("negativeresources", negativeResources);
        modelMap.addAttribute("positiveresources", positiveResources);
        modelMap.addAttribute("type", type);
        return "recommend/hotsearchkeyword";
    }

    @RequestMapping(value = "/admin/recommend/{type}/hotsearch/{operation}", method = RequestMethod.POST)
    public
    @ResponseBody
    Integer recommendHotSearch(@PathVariable String type, @PathVariable String operation, HttpServletRequest request) {
        String methodname = String.format("%sHotSearchKeyWordsByType", operation);
        String keywords = request.getParameter("keywords");
        System.out.println(keywords);
        String[] keywordArray = keywords.split(",");

        try {
            Method method = statisticsDao.getClass().getDeclaredMethod(methodname, new Class[]{String.class, String[].class});
            method.invoke(statisticsDao, new Object[]{type, keywordArray});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 200;
    }
}
