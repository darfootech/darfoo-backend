package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;

/**
 * Created by zjh on 14-11-26.
 */
public class DanceMusicCates {
    public static HashMap<Integer, String> letterCategories = new HashMap<Integer, String>();
    static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    static {
        for (int i = 0; i < letters.length; i++) {
            letterCategories.put(i + 1, letters[i]);
        }
    }
}
