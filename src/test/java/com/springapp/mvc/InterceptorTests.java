package com.springapp.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjh on 14-12-13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class InterceptorTests {
    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    @Test
    public void getNumberFromUri(){
        String uri = "/darfoobackend/rest/resources/video/33";
        System.out.println(uri.matches("(.*)/resources/video/\\d+$"));
        System.out.println(getNumbers(uri));

        String uri1 = "/darfoobackend/rest/resources/video/tutorial/333";
        System.out.println(uri1.matches("(.*)/resources/video/tutorial/\\d+$"));
        System.out.println(getNumbers(uri1));

        String uri2 = "/darfoobackend/rest/resources/music/33";
        System.out.println(uri2.matches("(.*)/resources/music/\\d+$"));
        System.out.println(getNumbers(uri2));
    }
}
