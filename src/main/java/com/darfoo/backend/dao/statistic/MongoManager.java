package com.darfoo.backend.dao.statistic;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.net.UnknownHostException;
import java.util.Arrays;

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
            String username = mongoConfig.getString("mongo.username");
            char[] password = mongoConfig.getString("mongo.password").toCharArray();

            try {
                MongoCredential credential = MongoCredential.createMongoCRCredential(username, "admin", password);
                mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return mongoClient;
    }
}
