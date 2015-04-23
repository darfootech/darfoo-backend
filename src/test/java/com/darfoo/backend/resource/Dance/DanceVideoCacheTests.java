package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.caches.dao.AccompanyCacheDao;
import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.service.responsemodel.SingleDanceVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zjh on 14-12-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class DanceVideoCacheTests {
    @Autowired
    AccompanyCacheDao accompanyCacheDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    CacheUtils cacheUtils;
    @Autowired
    RecommendDao recommendDao;

    @Test
    public void cacheSingleVideo() {
        SingleDanceVideo video = (SingleDanceVideo) cacheUtils.cacheSingleResource("video", 81);
        System.out.println(video);
    }

    @Test
    public void cacheIndexVideos() {
        List<SingleDanceVideo> videos = cacheUtils.cacheIndexResources("video");
        for (SingleDanceVideo video : videos) {
            System.out.println(video.toString());
        }
    }

    @Test
    public void cacheVideosByCategories() {
        String category = "0";

        List<SingleDanceVideo> videos = cacheUtils.cacheResourcesByCategory("video", category);
        for (SingleDanceVideo video : videos) {
            System.out.println(video);
        }
    }
}
