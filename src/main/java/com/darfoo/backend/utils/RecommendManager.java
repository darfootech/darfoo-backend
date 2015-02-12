package com.darfoo.backend.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zjh on 15-1-5.
 */
public class RecommendManager {
    public static Config recommendConfig;
    public static String basepath;

    //we just use database to specific recommend video ant tutorial and do not need configfactory any more
    /*static {
        recommendConfig = ConfigFactory.load("recommend");
        basepath = recommendConfig.getString("recommend.basepath");
    }*/
}
