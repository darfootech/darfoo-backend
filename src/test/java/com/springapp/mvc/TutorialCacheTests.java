package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    EducationDao educationDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    CommonRedisClient redisClient;

    @Test
    public void testDeleteTutorial(){
        String key = "tutorial-1";
        redisClient.delete(key);
    }

    @Test
    public void insertTutorial(){
        Education tutorial = educationDao.getEducationVideoById(1);
        System.out.println(tutorialCacheDao.insert(tutorial));
    }

    @Test
    public void getSingleTutorial(){
        Integer id = 1;
        SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(id);
        System.out.println(tutorial.getTitle());
    }

}
