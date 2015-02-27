package com.darfoo.backend.utils;

import com.darfoo.backend.caches.client.CommonRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by zjh on 14-12-27.
 */

@EnableScheduling
public class ScheduledTasks {
    @Autowired
    CommonRedisClient redisClient;

    //=>暂时一天清空一次redis缓存
    @Scheduled(fixedRate = 600000)
    public void flushRedisCache() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // S is the millisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = simpleDateFormat.format(timestamp).split(" ")[1];
        String currenthour = currenttime.split(":")[0];
        if (currenthour.equals("00")) {
            System.out.println("flushredis -> " + redisClient.deleteCurrentDB());
        } else {
            System.out.println(currenttime);
        }
    }
}
