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
public class InsertDaoTests {
    @Autowired
    CommonDao commonDao;

    @Test
    public void insertVideoResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String videoTitle = "videotitle-" + System.currentTimeMillis();
        String authorName = "王广成广场舞";
        String imagekey = String.format("dancevideo-imagekey-%s.%s", System.currentTimeMillis(), ".jpg");

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("title", videoTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("imagetype", "jpg");
        /*insertcontents.put("category1", "正面教学");
        insertcontents.put("category2", "口令分解");
        insertcontents.put("category3", "背面教学");
        insertcontents.put("category4", "队形教学");*/
        insertcontents.put("category", "");
        insertcontents.put("type", "tutorial");
        insertcontents.put("videotype", "mp4");
        insertcontents.put("connectmusic", "ccccc-memeda-33");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceVideo.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void insertMusicResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String musicTitle = "musictitle-" + System.currentTimeMillis();
        String authorName = "吉卉";

        insertcontents.put("title", musicTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("category", "D");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceMusic.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void insertDanceGroupResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String authorName = "周杰伦" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("name", authorName);
        insertcontents.put("imagetype", "png");
        insertcontents.put("description", "台湾人气偶像组合");
        insertcontents.put("type", "normal");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceGroup.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void insertOperaSeries() {
        Class resource = OperaSeries.class;
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String operaseriesname = "红楼梦";
        String imagetype = "png";
        String imagekey = String.format("%s-imagekey-%s.%s", resource.getSimpleName().toLowerCase(), System.currentTimeMillis(), imagetype);

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("title", operaseriesname);

        HashMap<String, Integer> insertresult = commonDao.insertResource(OperaSeries.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void insertOperaVideo() {
        Class resource = OperaVideo.class;
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String videoTitle = "videotitle-" + System.currentTimeMillis();
        //String videoTitle = "videotitle-1428809230001";
        String operaseriesname = "红楼梦";
        String imagekey = String.format("%s-imagekey-%s.%s", resource.getSimpleName().toLowerCase(), System.currentTimeMillis(), ".jpg");

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("title", videoTitle);
        /*insertcontents.put("seriesname", operaseriesname);
        insertcontents.put("type", "series");*/
        insertcontents.put("type", "single");
        insertcontents.put("videotype", "mp4");

        HashMap<String, Integer> insertresult = commonDao.insertResource(OperaVideo.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }
}
