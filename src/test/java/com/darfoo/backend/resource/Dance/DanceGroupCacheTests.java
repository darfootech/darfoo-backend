package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.service.responsemodel.SingleDanceGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class DanceGroupCacheTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    CacheUtils cacheUtils;

    @Test
    public void cacheSingleAuthor() {
        SingleDanceGroup author = (SingleDanceGroup) cacheUtils.cacheSingleResource("author", 39);
        System.out.println(author);
    }

    @Test
    public void cacheIndexAuthors() {
        List<SingleDanceGroup> authors = cacheUtils.cacheIndexResources("author");
        for (SingleDanceGroup author : authors) {
            System.out.println(author);
        }
    }
}
