package com.springapp.mvc;

import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 14-12-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/pre-deal.xml",
	"file:src/main/webapp/WEB-INF/redis-context.xml",
    "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"     
})
public class VideoCacheTests {
    @Autowired
    VideoDao videoDao;
    @Autowired
    VideoCacheDao videoCacheDao;

    @Test
    public void testAddVideo(){
        Video video = videoDao.getVideoByVideoId(1);
        boolean result = videoCacheDao.add(video);
        System.out.println(result);
    }

    @Test
    public void testDeletevideo(){
        String key = "1";
        videoCacheDao.delete(key);
    }
}
