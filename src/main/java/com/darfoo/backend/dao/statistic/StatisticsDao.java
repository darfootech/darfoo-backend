package com.darfoo.backend.dao.statistic;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zjh on 15-3-2.
 */
public class StatisticsDao {
    MongoClient client = MongoManager.getMongoClientInstance();
    DB db = client.getDB("statistics");

    private BasicDBObject createDBObject(Class resource, HashMap<String, Object> conditions) {
        BasicDBObject doc = new BasicDBObject();
        for (Field field : resource.getFields()) {
            String fieldname = field.getName();
            if (conditions.keySet().contains(fieldname)) {
                doc.append(fieldname, conditions.get(fieldname));
            } else {
                if (fieldname.equals("timestamp")) {
                    doc.append(fieldname, System.currentTimeMillis() / 1000);
                }
                if (fieldname.equals("date")) {
                    doc.append(fieldname, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }
        }
        return doc;
    }

    public void insertTimeBehavior(Class resource, HashMap<String, Object> conditions) {
        DBCollection coll = db.getCollection(resource.getSimpleName().toLowerCase());
        coll.insert(createDBObject(resource, conditions));
    }

    public void insertOrUpdateClickBehavior(Class resource, HashMap<String, Object> conditions) {
        DBCollection coll = db.getCollection(resource.getSimpleName().toLowerCase());
        BasicDBObject query = createDBObject(resource, conditions);

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
