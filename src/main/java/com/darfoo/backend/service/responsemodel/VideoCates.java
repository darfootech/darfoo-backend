package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-11-25.
 */
public class VideoCates {
    private Map<String, String> speedCategory = new HashMap<String, String>();
    private Map<String, String> difficultyCategory = new HashMap<String, String>();
    private Map<String, String> styleCategory = new HashMap<String, String>();

    private int speedCount = 4;
    private String[] speedArray = {"全部", "较快", "适中", "较慢"};
    private int difficultyCount = 4;
    private String[] difficultyArray = {"全部", "简单", "适中", "稍难"};
    private int styleCount =  12;
    private String[] styleArray = {"全部", "欢快", "活泼", "优美", "情歌风", "红歌风", "草原风", "戏曲风", "印巴风", "江南风", "民歌风", "儿歌风"};

    public VideoCates() {
        for (int i=0; i< speedCount; i++){
            speedCategory.put(i+"", speedArray[i]);
        }

        for (int i=0; i< difficultyCount; i++){
            difficultyCategory.put(i+"", difficultyArray[i]);
        }

        for (int i=0; i< styleCount; i++){
            styleCategory.put(i+"", styleArray[i]);
        }
    }

    public Map<String, String> getSpeedCategory() {
        return speedCategory;
    }

    public Map<String, String> getDifficultyCategory() {
        return difficultyCategory;
    }

    public Map<String, String> getStyleCategory() {
        return styleCategory;
    }
}
