package com.darfoo.backend.cache;

import com.darfoo.backend.caches.client.RedisManager;
import com.darfoo.backend.caches.dao.CacheUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-4-28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class PipelineTests {
    @Autowired
    CacheUtils cacheUtils;
    JedisPool jedisPool = RedisManager.getRedisPoolInstance();

    public void logResources(List resources) {
        for (Object object : resources) {
            System.out.println(object);
        }
        System.out.println("resources total size -> " + resources.size());
    }

    //5.382 seconds
    @Test
    public void normalInsert() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                String result = jedis.set("n" + i, "n" + i);
            }
            long end = System.currentTimeMillis();
            System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    //插入多个无依赖的数据使用pipeline可以非阻塞的并发执行 理论上可以得到性能提升
    //0.581 seconds
    //使用pipeline获得了10倍的性能提升
    //0.319 seconds
    @Test
    public void piplineInsert() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                pipeline.set("p" + i, "p" + i);
            }
            //List<Object> results = pipeline.syncAndReturnAll();
            long end = System.currentTimeMillis();
            System.out.println("Pipelined SET: " + ((end - start) / 1000.0) + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    //no pipeline 1.595 seconds
    //pipeline 1.487 seconds
    @Test
    public void cacheResourcesByInnertype() {
        HashMap<String, String[]> typeinnertypepair = new HashMap<String, String[]>();
        typeinnertypepair.put("dancevideo", new String[]{"NORMAL"});
        for (String type : typeinnertypepair.keySet()) {
            String[] innertypes = typeinnertypepair.get(type);
            for (String innertype : innertypes) {
                logResources(cacheUtils.cacheResourcesByInnertype(type, innertype));
            }
        }
    }
}
