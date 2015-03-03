package com.springapp.mvc.statistic;

import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by zjh on 15-3-3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class StatisticsClickTimeTests {
    @Autowired
    StatisticsDao statisticsDao;

    @Test
    public void insertResourceClickTime() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("mac", "00:ad:05:01:a6:85");
        conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
        conditions.put("uuid", "123");
        conditions.put("type", "video");
        conditions.put("resourceid", 81);

        statisticsDao.insertClickBehavior(ResourceClickTime.class, conditions);
    }

    @Test
    public void insertMenuClickCount() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("mac", "00:ad:05:01:a6:85");
        conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
        conditions.put("uuid", "123");
        conditions.put("menuid", 1);

        statisticsDao.insertClickBehavior(MenuClickTime.class, conditions);
    }
}
