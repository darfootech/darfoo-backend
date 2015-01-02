package com.darfoo.backend.utils;

import java.util.Date;

import com.darfoo.backend.caches.CommonRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by zjh on 14-12-27.
 */

@EnableScheduling
public class ScheduledTasks {
    @Autowired
    CommonRedisClient redisClient;

    /*@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("Method executed at every 5 seconds. Current time is :: "+ new Date());
    }*/

    //=>暂时一天清空一次redis缓存
    @Scheduled(fixedRate = 86400000)
    public void flushRedisCache(){
        System.out.println("flushredis -> " + redisClient.deleteCurrentDB());
    }
}
