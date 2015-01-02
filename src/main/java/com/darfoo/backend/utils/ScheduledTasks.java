package com.darfoo.backend.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    @Scheduled(fixedRate = 10000)
    public void flushRedisCache(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // S is the millisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = simpleDateFormat.format(timestamp).split(" ")[1];
        if (currenttime.equals("00:00:00")){
            System.out.println("flushredis -> " + redisClient.deleteCurrentDB());
        }else{
            System.out.println(currenttime);
        }
    }
}
