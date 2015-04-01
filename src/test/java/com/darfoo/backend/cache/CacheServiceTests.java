package com.darfoo.backend.cache;

import com.darfoo.backend.caches.dao.CacheUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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

    public void logResources(List resources) {
        for (Object object : resources) {
            System.out.println(object);
        }
    }

    @Test
    public void cacheSingleResource() {
        String type = "dancevideo";
        Integer id = 1073;
        System.out.println(cacheUtils.cacheSingleResource(type, id));
    }

    @Test
    public void cacheRecommendResources() {
        String type = "dancevideo";
        logResources(cacheUtils.cacheRecommendResources(type));
    }

    @Test
    public void cacheIndexResources() {
        String type = "dancevideo";
        logResources(cacheUtils.cacheIndexResources(type));
    }

    @Test
    public void cacheResourcesByCategory() {
        String type = "dancevideo";
        String category = "0";
        logResources(cacheUtils.cacheResourcesByCategory(type, category));
    }

    @Test
    public void cacheResourcesByCategoryByPage() {
        String type = "dancevideo";
        String category = "0";
        Integer page = 1;
        logResources(cacheUtils.cacheResourcesByCategory(type, category, page));
    }
}
