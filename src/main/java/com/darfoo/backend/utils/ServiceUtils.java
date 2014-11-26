package com.darfoo.backend.utils;

import java.util.List;

/**
 * Created by zjh on 14-11-26.
 */
public class ServiceUtils {
    public static String[] convertList2Array(List<String> vidoes) {
        String[] stockArr = new String[vidoes.size()];
        stockArr = vidoes.toArray(stockArr);
        return stockArr;
    }

}
