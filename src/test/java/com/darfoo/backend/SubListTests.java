package com.darfoo.backend;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zjh on 15-4-24.
 */
public class SubListTests {
    public void logResources(List resources) {
        for (Object object : resources) {
            System.out.println(object);
        }
        System.out.println("resources total size -> " + resources.size());
    }

    @Test
    public void cutSubList() {
        int skipNum = 0;
        int returnNum = 3;
        int start = skipNum;
        int end = skipNum + returnNum - 1;
        List lst = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        logResources(lst.subList(start, end + 1));
    }
}
