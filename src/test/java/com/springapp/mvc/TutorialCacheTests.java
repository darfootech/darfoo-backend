package com.springapp.mvc;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.service.responsemodel.TutorialCates;
import com.darfoo.backend.utils.ServiceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class TutorialCacheTests {
    @Autowired
    TutorialDao educationDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;

    TutorialCates tutorialCates = new TutorialCates();

    @Test
    public void testDeleteTutorial() {
        String key = "tutorial-1";
        redisClient.delete(key);
    }

    @Test
    public void insertTutorial() {
        Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, 1);
        System.out.println(tutorialCacheDao.insertSingleTutorial(tutorial));
    }

    @Test
    public void getSingleTutorial() {
        Integer id = 1;
        SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(id);
        System.out.println(tutorial.getTitle());
    }

    @Test
    public void cacheCategory() {
        String categories = "0-0-0-0";
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")) {
            String speedCate = tutorialCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")) {
            String difficultyCate = tutorialCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
        }
        if (!requestCategories[2].equals("0")) {
            String styleCate = tutorialCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }

        List<Tutorial> targetVideos = educationDao.getEducationVideosByCategories(ServiceUtils.convertList2Array(targetCategories));
        for (Tutorial video : targetVideos) {
            int vid = video.getId();
            long result = redisClient.sadd("tutorialcategory" + categories, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }
    }

    @Test
    public void getCategory() {
        String categories = "0-0-0-0";
        Set<String> categoryVideoKeys = redisClient.smembers("tutorialcategory" + categories);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : categoryVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        System.out.println(result.size());
    }
}
