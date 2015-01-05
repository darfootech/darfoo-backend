package com.darfoo.backend.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zjh on 15-1-5.
 */
public class RecommendManager {
    public static Config recommendConfig;
    public static String basepath;

    static {
        recommendConfig = ConfigFactory.load("recommend");
        basepath = recommendConfig.getString("recommend.basepath");
    }
}
