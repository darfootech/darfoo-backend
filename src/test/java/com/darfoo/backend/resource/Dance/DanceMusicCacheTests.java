package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.service.responsemodel.SingleDanceMusic;
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
public class DanceMusicCacheTests {
    @Autowired
    CacheUtils cacheUtils;

    @Test
    public void cacheSingleMusic() {
        SingleDanceMusic music = (SingleDanceMusic) cacheUtils.cacheSingleResource("dancemusic", 39);
        System.out.println(music);
    }

    @Test
    public void cacheMusicsByCategories() {
        String categories = "A";
        List<SingleDanceMusic> musics = cacheUtils.cacheResourcesByCategory("dancemusic", categories);
        for (SingleDanceMusic music : musics) {
            System.out.println(music);
        }
    }

    @Test
    public void cacheHottestMusics() {
        List<SingleDanceMusic> musics = cacheUtils.cacheHotResources("dancemusic");
        for (SingleDanceMusic music : musics) {
            System.out.println(music);
        }
    }
}
