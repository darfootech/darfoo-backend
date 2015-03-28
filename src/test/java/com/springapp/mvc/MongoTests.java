package com.springapp.mvc;

import com.darfoo.backend.dao.statistic.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zjh on 15-3-26.
 */

public class MongoTests {
    MongoClient client = MongoManager.getMongoClientInstance();
    DB db = client.getDB("statistics");
    DBCollection coll = db.getCollection("resourceclickcount");

    @Test
    public void testInsert() {
        BasicDBObject doc = new BasicDBObject()
                .append("hostip", "fe80::2ad:5ff:fe01:a685-wlan0")
                .append("mac", "00:ad:05:01:a6:8533")
                .append("uuid", "333333333333")
                .append("resourceid", 33)
                .append("type", "dancevideo")
                .append("timestamp", System.currentTimeMillis() / 1000)
                .append("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        coll.insert(doc);
    }

    @Test
    public void testInsertWithInc() {
        BasicDBObject query = new BasicDBObject()
                .append("hostip", "fe80::2ad:5ff:fe01:a685-wlan0")
                .append("mac", "00:ad:05:01:a6:8533")
                .append("uuid", "333333333333")
                .append("resourceid", 33)
                .append("type", "dancevideo");


        if (coll.find(query).count() > 0) {
            System.out.println("记录已经存在");
            BasicDBObject update = new BasicDBObject()
                    .append("$inc", new BasicDBObject("hot", 1L));
            coll.update(query, update);
        } else {
            System.out.println("记录还不存在");
            BasicDBObject doc = query.append("hot", 1L);
            coll.insert(doc);
        }
    }
}
