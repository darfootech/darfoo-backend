package com.springapp.mvc.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zjh on 14-12-30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/redis-context.xml")
public class RedisPoolTests {
    @Autowired
    JedisPool jedisPool;

    @Test
    public void getVideoObject() {
        Jedis jedis = null;
        try {
            int vid = 1;
            String key = "video-" + vid;
            jedis = jedisPool.getResource();
            String title = jedis.hget(key, "title");
            String authorname = jedis.hget(key, "authorname");
            String videourl = jedis.hget(key, "videourl");
            String imageurl = jedis.hget(key, "imageurl");

            System.out.println(title + " - " + authorname + " - " + videourl + " - " + imageurl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
