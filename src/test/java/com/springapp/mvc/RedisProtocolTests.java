package com.springapp.mvc;

import com.darfoo.backend.caches.CacheInsertProtocol;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-2-14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class RedisProtocolTests {
    @Autowired
    VideoDao videoDao;
    @Autowired
    CacheInsertProtocol cacheInsertProtocol;

    @Test
    public void insertProtocol() {
        Video video = videoDao.getVideoByVideoId(35);
        cacheInsertProtocol.insertResourceIntoCache(Video.class, video);
    }
}
