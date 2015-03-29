package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
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
    VideoCacheDao videoCacheDao;
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
        SingleVideo video = (SingleVideo) cacheUtils.cacheSingleResource("video", 81);
        System.out.println(video);
    }

    @Test
    public void cacheIndexVideos() {
        List<SingleVideo> videos = cacheUtils.cacheIndexResources("video");
        for (SingleVideo video : videos) {
            System.out.println(video.toString());
        }
    }

    @Test
    public void cacheRecommendVideos() {
        String cachekey = "recommend";

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);

            List recommendResources = recommendDao.getRecommendResources(resource);
            cacheDao.insertResourcesIntoCache(resource, recommendResources, cachekey, type, CacheCollType.SET);
        }

        List<SingleVideo> videos = cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);

        for (SingleVideo video : videos) {
            System.out.println(video.toString());
        }
    }

    @Test
    public void cacheVideosByCategories() {
        String categories = "0-0-0-0";

        List<SingleVideo> videos = cacheUtils.cacheResourcesByCategories("video", categories);
        for (SingleVideo video : videos) {
            System.out.println(video);
        }
    }

    @Test
    public void cacheVideoMusic() {
        int id = 81;
        String type = "music";
        Class resource = TypeClassMapping.typeClassMap.get(type);
        DanceMusic targetMusic = ((DanceVideo) commonDao.getResourceById(DanceVideo.class, id)).getMusic();
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Object object = commonDao.getResourceById(resource, music_id);
            cacheDao.insertSingleResource(resource, object, type);
            SingleMusic music = (SingleMusic) cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), type);
            System.out.println(music);
        } else {
            System.out.println("there is no music attach to the target video");
        }
    }
}
