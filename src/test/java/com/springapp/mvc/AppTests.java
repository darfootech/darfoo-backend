package com.springapp.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.model.DanceGroupImage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AppTests {
    @Test
    public void testMap2Json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "mkyong");
        map.put("age", "29");

        //convert map to JSON string
        try {
            json = mapper.writeValueAsString(map);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testsplit() {
        String connectmusic = "呵呵呵--10";
        System.out.println(connectmusic.split("-")[2]);
    }
}
