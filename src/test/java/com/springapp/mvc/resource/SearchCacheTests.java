package com.springapp.mvc.resource;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class SearchCacheTests {
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    CommonDao commonDao;
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
