package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-11-25.
 */
public class TutorialCates {
    private Map<String, String> speedCategory = new HashMap<String, String>();
    private Map<String, String> difficultyCategory = new HashMap<String, String>();
    private Map<String, String> styleCategory = new HashMap<String, String>();
    private Map<String, String> teacherCategory = new HashMap<String, String>();

    private int speedCount = 4;
    private String[] speedArray = {"全部", "快", "中", "慢"};
    private int difficultyCount = 4;
    private String[] difficultyArray = {"全部", "简单", "适中", "稍难"};
    private int styleCount = 4;
    private String[] styleArray = {"全部", "队形表演", "背面教学", "分解教学"};
    private int teacherCount = 4;
    private String[] teacherArray = {"全部", "teacher1", "teacher2", "teacher3"};

    public TutorialCates() {
        for (int i = 0; i < speedCount; i++) {
            speedCategory.put(i + "", speedArray[i]);
        }

        for (int i = 0; i < difficultyCount; i++) {
            difficultyCategory.put(i + "", difficultyArray[i]);
        }

        for (int i = 0; i < styleCount; i++) {
            styleCategory.put(i + "", styleArray[i]);
        }

        for (int i = 0; i < teacherCount; i++) {
            teacherCategory.put(i + "", teacherArray[i]);
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

    public Map<String, String> getTeacherCategory() {
        return teacherCategory;
    }
}
