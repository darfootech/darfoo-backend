package com.darfoo.backend.statistic;

/**
 * Created by zjh on 15-3-2.
 */

import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.DanceSearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class StatisticsTests {
    @Autowired
    StatisticsDao statisticsDao;

    public void logResources(List resources) {
        for (Object object : resources) {
            System.out.println(object);
        }
        System.out.println("resources total size -> " + resources.size());
    }

    @Test
    public void insertOrUpdateResourceClickCount() {
        String[] types = {"dancevideo", "dancemusic", "dancegroup", "operavideo", "operaseries"};
        for (String type : types) {
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("type", type);
            conditions.put("resourceid", 33);
            conditions.put("title", "呵呵");

            statisticsDao.insertOrUpdateClickBehavior(ResourceClickCount.class, conditions);
        }
    }

    @Test
    public void insertOrUpdateMenuClickCount() {
        Integer[] menus = {1, 2, 3, 4, 5, 6, 7, 8};
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        for (Integer menu : menus) {
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("menuid", menu);
            conditions.put("title", "呵呵");

            statisticsDao.insertOrUpdateClickBehavior(MenuClickCount.class, conditions);
        }
    }

    @Test
    public void insertOrUpdateTabClickCount() {
        Integer[] tabs = {1, 2, 3, 4, 5, 6, 7};
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        for (Integer tab : tabs) {
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("tabid", tab);
            conditions.put("title", "呵呵");

            statisticsDao.insertOrUpdateClickBehavior(TabClickCount.class, conditions);
        }
    }

    @Test
    public void insertResourceClickTime() {
        String[] types = {"dancevideo", "dancemusic", "dancegroup", "operavideo", "operaseries"};
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        for (String type : types) {
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("type", type);
            conditions.put("resourceid", 81);
            conditions.put("title", "呵呵");

            statisticsDao.insertTimeBehavior(ResourceClickTime.class, conditions);
        }
    }

    @Test
    public void insertMenuClickTime() {
        Integer[] menus = {1, 2, 3, 4, 5, 6, 7, 8};
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        for (Integer menu : menus) {
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("menuid", menu);
            conditions.put("title", "呵呵");

            statisticsDao.insertTimeBehavior(MenuClickTime.class, conditions);
        }
    }

    @Test
    public void insertTabClickTime() {
        Integer[] tabs = {1, 2, 3, 4, 5, 6, 7};
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        for (Integer tab : tabs) {
            conditions.put("mac", "00:ad:05:01:a6:85");
            conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
            conditions.put("uuid", "123");
            conditions.put("tabid", tab);
            conditions.put("title", "呵呵");

            statisticsDao.insertTimeBehavior(TabClickTime.class, conditions);
        }
    }

    @Test
    public void insertSearchBehavior() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("searchcontent", "呵呵");
        conditions.put("searchtype", "video");

        statisticsDao.insertTimeBehavior(DanceSearchHistory.class, conditions);
    }

    @Test
    public void insertCrashLog() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("log", "exception 呵呵");

        statisticsDao.insertTimeBehavior(CrashLog.class, conditions);
    }

    @Test
    public void hotSearchKeyWords() {
        String type = "dance";
        logResources(statisticsDao.getSearchKeyWordsOrderByTypeByHot(type));
    }

    private void operateHotSearch(String operation, String[] keywordArray) {
        String methodname = String.format("%sHotSearchKeyWords", operation);

        try {
            Method method = statisticsDao.getClass().getDeclaredMethod(methodname, new Class[]{String[].class});
            method.invoke(statisticsDao, new Object[]{keywordArray});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertHotSearch() {
        operateHotSearch("insert", new String[]{"艺子龙", "佳木斯", "依依"});
    }

    @Test
    public void removeHotSearch() {
        operateHotSearch("remove", new String[]{"艺子龙", "佳木斯", "依依"});
    }

    @Test
    public void getHotSearch() {
        String type = "dance";
        logResources(statisticsDao.getHotSearchKeyWordsByType(type));
    }
}
