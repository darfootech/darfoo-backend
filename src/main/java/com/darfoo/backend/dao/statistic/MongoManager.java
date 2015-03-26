package com.darfoo.backend.dao.statistic;

import com.mongodb.MongoClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.UnknownHostException;

/**
 * Created by zjh on 15-3-26.
 */
public class MongoManager {
    public static MongoClient mongoClient;
    public static Config mongoConfig;

    static {
        mongoConfig = ConfigFactory.load("mongodb");
    }

    public static MongoClient getMongoClientInstance() {
        if (mongoClient == null) {
            String host = mongoConfig.getString("mongo.host");
            Integer port = mongoConfig.getInt("mongo.port");

            try {
                mongoClient = new MongoClient(host, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return mongoClient;
    }
}
