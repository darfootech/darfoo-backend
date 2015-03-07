package com.darfoo.backend.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zjh on 15-3-7.
 */
public class UploadDirConfig {
    public static Config uploaddirConfig;
    public static String uploaddir;

    static {
        uploaddirConfig = ConfigFactory.load("uploaddir");
        uploaddir = uploaddirConfig.getString("upload.dir");
    }

    public static String getBackendBaseUrl() {
        if (uploaddir == null) {
            uploaddir = uploaddirConfig.getString("backend.baseurl");
        }
        return uploaddir;
    }
}
