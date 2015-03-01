package com.springapp.mvc.resource;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.MusicCates;
import com.darfoo.backend.service.responsemodel.SingleMusic;
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
public class MusicCacheTests {
    @Autowired
    CacheDao cacheDao;

    @Test
    public void cacheSingleMusic() {
        SingleMusic music = (SingleMusic) new CacheDaoTests().cacheSingleResource("music", 39);
        System.out.println(music);
    }

    @Test
    public void cacheMusicsByCategories() {
        String categories = "1-0-0";
        List<SingleMusic> musics = new CacheDaoTests().cacheResourcesByCategories("music", categories);
        for (SingleMusic music : musics) {
            System.out.println(music);
        }
    }

    @Test
    public void cacheHottestMusics() {
        List<SingleMusic> musics = new CacheDaoTests().cacheHottestResources("music", 5);
        for (SingleMusic music : musics) {
            System.out.println(music);
        }
    }
}
