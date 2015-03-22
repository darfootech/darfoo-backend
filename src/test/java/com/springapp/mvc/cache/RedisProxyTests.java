package com.springapp.mvc.cache;

import com.darfoo.backend.caches.client.CommonRedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by YY_410 on 2015/3/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class RedisProxyTests {

    @Autowired
    CommonRedisClient commonRedisClient;

    @Autowired
    RedisTemplate redisTemplate;

    //单数据String简单测试
    @Test
    public void setAndGetString() {
        String key = "1", value = "1";
        commonRedisClient.set(key, value);
        if (commonRedisClient.exists(key)) {
            System.out.println(key + "=" + commonRedisClient.get(key));
        } else {
            System.out.println(key + "not found");
        }
    }

    //测试短时间写入大量数据
    @Test
    public void setAndGetMultiString() {
        String key = "jihui", value = "jihui";
        int num = 8000;
        int count = 0;
        long start = System.currentTimeMillis();
        while (num-- > 6000) {
            count++;
            try {
                commonRedisClient.set(key + "-" + num, value + "-" + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) + "ms\n" + "插入key-value:" + count);
    }

    //测试hashmap作为value的读写
    @Test
    public void setAndGetHashString() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String key = "map-1";
        hashMap.put("name", "ji-hui");
        hashMap.put("age", "24");
        hashMap.put("sex", "male");
        commonRedisClient.hmset(key, hashMap);
        if (commonRedisClient.exists(key)) {
            HashMap<String, String> hashMapRes = (HashMap<String, String>) commonRedisClient.hgetAll(key);
            printMap(hashMapRes);
        } else {
            System.out.println("key:" + key + " not found");
        }

    }

    //测试List的读写
    @Test
    public void setAndGetListString() {
        String key = "list-1";
        if (commonRedisClient.exists(key)) {
            commonRedisClient.delete(key);
        }
        for (int i = 0; i < 1000; i++) {
            //System.out.print(" " + commonRedisClient.lpush(key, "" + i));
            System.out.print(" " + redisTemplate.opsForList().rightPush(key, "" + i));
            if ((i + 1) % 20 == 0)
                System.out.println();
        }
    }

    //测试List lrange(先运行setAndGetListString)
    @Test
    public void getSubListRangeFrom() {
        String key = "list-1";
        List<String> list = commonRedisClient.lrange(key, 0l, redisTemplate.opsForList().size(key));
        System.out.println(list);
    }

    //测试List 按位置获取List的值
    @Test
    public void getValueHeadOrTail() {
        String key = "list-1";
        System.out.println(redisTemplate.opsForList().index(key, 0));
        System.out.println(redisTemplate.opsForList().index(key, 800));
        System.out.println(redisTemplate.opsForList().index(key, redisTemplate.opsForList().size(key) - 1));
        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().rightPop(key));
    }

    //测试Set sadd&smember
    @Test
    public void setAndGetSet() {
        String key = "set-1";
        for (int i = 0; i < 100; i++) {
            commonRedisClient.sadd(key, "" + i);
        }
        Set<String> setRes = commonRedisClient.smembers(key);
        System.out.println(setRes);
    }

    //测试获取所有的key
    //Twemproxy好像不支持，会报错
    @Test
    public void showAllKeys() {
        String keyPattern = "info";
        Set<String> allKeys = redisTemplate.keys(keyPattern);
        System.out.print(allKeys.size());
        System.out.println(redisTemplate.hasKey("info"));
    }

    //测试删除dump
    //Twemproxy好像不支持，会报错
    //试试使用expire过期时间来代替deleteall
    @Test
    public void deleteAllDb() {
        commonRedisClient.deleteCurrentDB();
    }

    //打印单个map
    public void printMap(Map<String, String> map) {
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println(key + ":" + map.get(key));
        }
    }

}
