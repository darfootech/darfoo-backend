package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-11-26.
 */
public class MusicCates {
    private Map<String, String> beatCategory = new HashMap<String, String>();
    private Map<String, String> styleCategory = new HashMap<String, String>();

    private int beatCount = 5;
    private String[] beatArray = {"全部", "四拍", "八拍", "十六拍", "三十二拍"};
    private int styleCount = 9;
    private String[] styleArray = {"全部", "情歌风", "红歌风", "草原风", "戏曲风", "印巴风", "江南风", "民歌风", "儿歌风"};

    public MusicCates() {
        for (int i=0; i<beatCount; i++){
            beatCategory.put(i+"", beatArray[i]);
        }

        for (int i=0; i<styleCount; i++){
            styleCategory.put(i+"", styleArray[i]);
        }
    }

    public Map<String, String> getBeatCategory() {
        return beatCategory;
    }

    public Map<String, String> getStyleCategory() {
        return styleCategory;
    }
}
