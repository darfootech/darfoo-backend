package com.darfoo.backend.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by zjh on 15-4-1.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UpdateDaoTests {
    @Autowired
    CommonDao commonDao;

    @Test
    public void updateVideoById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String videoTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "王广成广场舞";

        Integer id = 1055;

        updatecontents.put("title", videoTitle);
        updatecontents.put("authorname", authorName);
        //updatecontents.put("category1", "正面教学");
        //updatecontents.put("category2", "口令分解");
        //updatecontents.put("category3", "背面教学");
        //updatecontents.put("category4", "队形教学");
        updatecontents.put("type", "normal");
        updatecontents.put("category", "");
        updatecontents.put("connectmusic", "");

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceVideo.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void updateMusicById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String musicTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "仓木麻衣";

        Integer id = 430;

        updatecontents.put("title", musicTitle);
        updatecontents.put("authorname", authorName);
        updatecontents.put("category", "q".toUpperCase());

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceMusic.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void updateDanceGroupById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String authorName = "滨崎步" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        Integer id = 110;

        updatecontents.put("name", authorName);
        updatecontents.put("imagekey", imagekey);
        updatecontents.put("description", "日本女歌手");

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceGroup.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void updateOperaSeriesById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String seriesname = "墨梅";

        Integer id = 1;

        updatecontents.put("title", seriesname);

        HashMap<String, Integer> insertresult = commonDao.updateResource(OperaSeries.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void updateOperaVideoById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        //String videoTitle = "么么-" + System.currentTimeMillis();
        String videoTitle = "哈哈";
        String seriesname = "红楼梦";

        Integer id = 4;

        updatecontents.put("title", videoTitle);
        updatecontents.put("seriesname", seriesname);
        updatecontents.put("type", "series");
        updatecontents.put("order", "1");

        HashMap<String, Integer> insertresult = commonDao.updateResource(OperaVideo.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }
}
