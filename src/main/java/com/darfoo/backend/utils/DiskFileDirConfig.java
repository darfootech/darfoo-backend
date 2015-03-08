package com.darfoo.backend.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zjh on 15-3-7.
 */
public class DiskFileDirConfig {
    public static Config dirConfig;
    public static String uploaddir;
    public static String csvdir;

    static {
        dirConfig = ConfigFactory.load("uploaddir");
        uploaddir = dirConfig.getString("upload.dir");
        csvdir = dirConfig.getString("csvfile.dir");
    }
}
