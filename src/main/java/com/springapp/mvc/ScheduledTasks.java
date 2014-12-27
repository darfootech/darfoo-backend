package com.springapp.mvc;

import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by zjh on 14-12-27.
 */

//@EnableScheduling
public class ScheduledTasks {

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("Method executed at every 5 seconds. Current time is :: "+ new Date());
    }
}
