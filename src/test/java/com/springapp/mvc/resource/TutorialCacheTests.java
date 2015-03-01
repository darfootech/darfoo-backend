package com.springapp.mvc.resource;

import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
    CacheUtils cacheUtils;

    @Test
    public void cacheSingleTutorial() {
        SingleVideo tutorial = (SingleVideo) cacheUtils.cacheSingleResource("tutorial", 36);
        System.out.println(tutorial);
    }

    @Test
    public void cacheTutorialsByCategories() {
        String categories = "0-0-0-0";

        List<SingleVideo> videos = cacheUtils.cacheResourcesByCategories("tutorial", categories);
        for (SingleVideo video : videos) {
            System.out.println(video);
        }
    }
}
