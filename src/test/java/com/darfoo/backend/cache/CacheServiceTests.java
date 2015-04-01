package com.darfoo.backend.cache;

import com.darfoo.backend.caches.dao.CacheUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-4-1.
 */

//测试所有和缓存层交互的service的正确性
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class CacheServiceTests {
    @Autowired
    CacheUtils cacheUtils;

    @Test
    public void cacheSingleResource() {
        String type = "dancevideo";
        Integer id = 1075;
        System.out.println(cacheUtils.cacheSingleResource(type, id));
    }
}
