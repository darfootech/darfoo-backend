package com.darfoo.backend.resource;

import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class SearchCacheTests {
    @Autowired
    CacheUtils cacheUtils;

    @Test
    public void cacheVideosBySearch() {
        String searchContent = "么么";
        List<SingleVideo> videos = cacheUtils.cacheResourcesBySearch("video", searchContent);
        for (SingleVideo video : videos) {
            System.out.println(video);
        }
    }

    @Test
    public void cacheMusicsBySearch() {
        String searchContent = "呵呵";
        List<SingleMusic> musics = cacheUtils.cacheResourcesBySearch("music", searchContent);
        for (SingleMusic music : musics) {
            System.out.println(music);
        }
    }
}

