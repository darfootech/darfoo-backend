package com.springapp.mvc.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.cota.ModelAttrSuper;
import com.darfoo.backend.model.statistics.mysql.clicktime.TabClickTime;
import com.darfoo.backend.model.statistics.mysql.CrashLog;
import com.darfoo.backend.model.statistics.mysql.SearchHistory;
import com.darfoo.backend.model.statistics.mysql.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.mysql.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.mysql.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.mysql.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.mysql.clickcount.TabClickCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-3-28.
 */

//将rds中的统计数据导入到mongodb中
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MigrateStatisticsData {
    @Autowired
    CommonDao commonDao;
    @Autowired
    StatisticsDao statisticsDao;

    public HashMap<String, Object> getConditionFromModel(Class resource, Object object) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();

        for (Field field : resource.getFields()) {
            String fieldname = field.getName().toLowerCase();
            Object value;

            if (fieldname.equals("id")) continue;
            if (field.isAnnotationPresent(ModelAttrSuper.class)) {
                value = commonDao.getResourceAttr(resource.getSuperclass(), object, fieldname);
            } else {
                value = commonDao.getResourceAttr(resource, object, fieldname);
            }
            if (fieldname.equals("date")) {
                conditions.put(fieldname, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
            } else {
                conditions.put(fieldname, value);
            }
        }
        return conditions;
    }


    @Test
    public void dumpDataFromMysqlToMongo() {
        Class[] resources = {ResourceClickCount.class, MenuClickCount.class, TabClickCount.class, ResourceClickTime.class, MenuClickTime.class, TabClickTime.class, CrashLog.class, SearchHistory.class};

        List result = commonDao.getAllResource(ResourceClickCount.class);

        for (Object object : result) {
            HashMap<String, Object> conditions = getConditionFromModel(ResourceClickCount.class, object);
            for (String key : conditions.keySet()) {
                System.out.println(key + conditions.get(key));
            }
            //insertTimeBehavior实际上就是直接插入
            statisticsDao.insertTimeBehavior(ResourceClickCount.class, conditions);
        }
    }
}
