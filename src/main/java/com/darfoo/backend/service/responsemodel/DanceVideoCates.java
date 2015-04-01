package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;

/**
 * Created by zjh on 14-11-25.
 */
public class DanceVideoCates {
    public static HashMap<String, String> danceVideoCategoryMap = new HashMap<String, String>();

    static {
        danceVideoCategoryMap.put("0", "正面教学");
        danceVideoCategoryMap.put("1", "口令分解");
        danceVideoCategoryMap.put("2", "背面教学");
        danceVideoCategoryMap.put("3", "队形教学");
    }
}
