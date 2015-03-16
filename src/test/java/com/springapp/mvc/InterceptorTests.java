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
    public void getNumberFromUri() {
        String uri = String.format("/darfoobackend/rest/resources/%s/33", "video");
        System.out.println(uri.matches(String.format("(.*)/resources/%s/\\d+$", "video")));
        System.out.println(getNumbers(uri));
    }
}
