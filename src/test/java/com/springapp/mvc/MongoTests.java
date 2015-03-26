package com.springapp.mvc;

import com.darfoo.backend.dao.statistic.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Test;

/**
 * Created by zjh on 15-3-26.
 */

public class MongoTests {
    MongoClient client = MongoManager.getMongoClientInstance();
    DB db = client.getDB("statistics");

    @Test
    public void testInsert() {
        DBCollection coll = db.getCollection("resourceclickcount");
        BasicDBObject doc = new BasicDBObject()
                .append("type", "database")
                .append("count", 1)
                .append("info", new BasicDBObject("x", 203).append("y", 102));
        coll.insert(doc);
    }

    @Test
    public void testInsertWithInc() {

    }
}
