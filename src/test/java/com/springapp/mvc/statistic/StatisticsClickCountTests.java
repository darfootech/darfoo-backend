package com.springapp.mvc.statistic;

/**
 * Created by zjh on 15-3-2.
 */

import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.statistics.ResourceClickCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class StatisticsClickCountTests {
    @Autowired
    StatisticsDao statisticsDao;

    @Test
    public void insertOrUpdateClickCount() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("mac", "00:ad:05:01:a6:85");
        conditions.put("hostip", "fe80::2ad:5ff:fe01:a685-wlan0");
        conditions.put("uuid", 123);
        conditions.put("type", "video");
        conditions.put("resourceid", 81);

        statisticsDao.insertOrUpdateClickBehavior(ResourceClickCount.class, conditions);
    }
}
