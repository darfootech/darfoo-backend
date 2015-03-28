package com.darfoo.backend.service;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.statistics.mongo.CrashLog;
import com.darfoo.backend.model.statistics.mongo.SearchHistory;
import com.darfoo.backend.model.statistics.mongo.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.mongo.clicktime.ResourceClickTime;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

            statisticsDao.insertTimeBehavior(TypeClassMapping.clickTimeStatMap.get(type), conditions);
            statisticsDao.insertOrUpdateClickBehavior(TypeClassMapping.clickCountStatMap.get(type), conditions);
        } else {
            conditions.put("type", type);
            conditions.put("resourceid", id);

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

        statisticsDao.insertTimeBehavior(SearchHistory.class, conditions);
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
}
