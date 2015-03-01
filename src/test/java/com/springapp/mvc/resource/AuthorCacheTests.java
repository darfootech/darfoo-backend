package com.springapp.mvc.resource;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-1-3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class AuthorCacheTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    CacheUtils cacheUtils;

    @Test
    public void cacheSingleAuthor() {
        SingleAuthor author = (SingleAuthor) cacheUtils.cacheSingleResource("author", 39);
        System.out.println(author);
    }

    @Test
    public void cacheIndexAuthors() {
        List<SingleAuthor> authors = cacheUtils.cacheIndexResources("author");
        for (SingleAuthor author : authors) {
            System.out.println(author);
        }
    }

    @Test
    public void cacheAuthorVideos() {
        int id = 11;

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);
        String cachekey = String.format("authorvideos%d", id);

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourcesByFields(resource, conditions);

            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        }

        List<SingleVideo> videos = cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);
        for (SingleVideo video : videos) {
            System.out.println(video);
        }
    }
}
