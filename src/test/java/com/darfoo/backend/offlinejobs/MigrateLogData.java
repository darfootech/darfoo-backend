package com.darfoo.backend.offlinejobs;

import com.darfoo.backend.dao.statistic.MongoManager;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.DanceSearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
import com.mongodb.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-4-4.
 */

//数据结构调整过之后需要从mongo中把之前的log数据导入到新的mongo的db中
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MigrateLogData {
    MongoClient client = MongoManager.getMongoClientInstance();
    DB oldDB = client.getDB("statistics");
    DB newDB = client.getDB("darfoolog");

    private DBObject updateTypeVal(DBObject object, String field) {
        Object type = object.get(field);
        if (type != null) {
            String typeVal = (String) type;
            if (typeVal.equals("video") || typeVal.equals("tutorial")) {
                object.put(field, "dancevideo");
            }
            if (typeVal.equals("music")) {
                object.put(field, "dancemusic");
            }
            if (typeVal.equals("author")) {
                object.put(field, "dancegroup");
            }
            return object;
        } else {
            return object;
        }
    }

    @Test
    public void migrateLogData() {
        Class[] classes = {
                ResourceClickCount.class,
                ResourceClickTime.class,
                MenuClickCount.class,
                MenuClickTime.class,
                TabClickCount.class,
                TabClickTime.class,
                DanceSearchHistory.class,
                CrashLog.class
        };

        for (Class c : classes) {
            String collectionName = c.getSimpleName().toLowerCase();
            DBCollection oldCollection = oldDB.getCollection(collectionName);
            DBCollection newCollection = newDB.getCollection(collectionName);

            DBCursor cursor = oldCollection.find();

            while (cursor.hasNext()) {
                DBObject object = updateTypeVal(updateTypeVal(cursor.next(), "type"), "searchtype");
                System.out.println(object);
                newCollection.insert(object);
            }
        }
    }
}
