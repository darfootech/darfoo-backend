package com.darfoo.backend.dao.statistic;

import com.mongodb.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjh on 15-3-2.
 */
public class StatisticsDao {
    MongoClient client = MongoManager.getMongoClientInstance();
    DB db = client.getDB("darfoolog");

    public DBCursor getAllStatisticData(Class resource) {
        DBCollection coll = db.getCollection(resource.getSimpleName().toLowerCase());
        boolean hasDateField = false;
        for (Field field : resource.getFields()) {
            if (field.getName().equals("date")) {
                hasDateField = true;
            }
        }
        if (hasDateField) {
            return coll.find().sort(new BasicDBObject("date", -1));
        } else {
            return coll.find();
        }
    }

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

    //根据搜索热度倒排序得到所有的搜索关键词
    public List getSearchKeyWordsOrderByTypeByHot(String type) {
        DBCollection collection = db.getCollection(String.format("%ssearchhistory", type));

        List<String> keywords = new ArrayList<String>();
        List hotsearchKeyWords = getHotSearchKeyWordsByType(type);

        Pattern whitespace = Pattern.compile("\\s+|\\?+|[a-zA-Z\\d\\+]+");
        Pattern special = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

        DBObject groupFields = new BasicDBObject("_id", "$searchcontent");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        AggregationOutput output = collection.aggregate(
                new BasicDBObject("$group", groupFields),
                new BasicDBObject("$sort", new BasicDBObject("count", -1)));

        for (DBObject obj : output.results()) {
            String content = (String) obj.get("_id");
            Integer count = (Integer) obj.get("count");

            Matcher whitespaceMatcher = whitespace.matcher(content);
            Matcher specialMatcher = special.matcher(content);

            if (!whitespaceMatcher.find() && !specialMatcher.find() && !hotsearchKeyWords.contains(content)) {
                System.out.println(content + "--->" + count);
                keywords.add(content);
            }
        }
        return keywords;
    }

    public List getHotSearchKeyWordsByType(String type) {
        List<String> keywords = new ArrayList<String>();
        DBCollection hotsearch = db.getCollection(String.format("%shotsearch", type));
        DBCursor cursor = hotsearch.find();
        while (cursor.hasNext()) {
            keywords.add((String) cursor.next().get("keyword"));
        }
        return keywords;
    }

    public void insertHotSearchKeyWordsByType(String type, String[] keywords) {
        DBCollection hotsearch = db.getCollection(String.format("%shotsearch", type));
        for (String keyword : keywords) {
            hotsearch.insert(new BasicDBObject().append("keyword", keyword));
        }
    }

    public void removeHotSearchKeyWordsByType(String type, String[] keywords) {
        DBCollection hotsearch = db.getCollection(String.format("%shotsearch", type));
        for (String keyword : keywords) {
            hotsearch.remove(new BasicDBObject().append("keyword", keyword));
        }
    }
}
