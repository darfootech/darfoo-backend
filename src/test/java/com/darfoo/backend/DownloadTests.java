package com.darfoo.backend;

/**
 * Created by zjh on 14-12-11.
 */

import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.DanceSearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
import com.darfoo.backend.utils.DownloadUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class DownloadTests {
    @Autowired
    DownloadUtils downloadUtils;

    @Test
    public void writeResourcesToCSV() {
        //Class[] classes = {DanceVideo.class, DanceMusic.class, DanceGroup.class};
        //Class[] classes = {OperaVideo.class, OperaSeries.class};
        Class[] classes = {DanceMusic.class};
        for (Class c : classes) {
            downloadUtils.writeResourcesToCSV(c);
        }
    }

    @Test
    public void writeStatisticsRecordsToCSV() {
        Class[] classes = {
                ResourceClickCount.class,
                ResourceClickTime.class,
                MenuClickCount.class,
                MenuClickTime.class,
                TabClickCount.class,
                TabClickTime.class,
                CrashLog.class,
                DanceSearchHistory.class
        };
        for (Class c : classes) {
            downloadUtils.writeStatisticDataToCSV(c);
        }
    }

    @Test
    public void writeVideosOfAuthorToCSV() {
        HashMap<Class, Integer> resourceidpair = new HashMap<Class, Integer>();
        resourceidpair.put(DanceGroup.class, 109);
        resourceidpair.put(OperaSeries.class, 3);
        for (Class resource : resourceidpair.keySet()) {
            downloadUtils.writeVideosOfResourceToCSV(resource, resourceidpair.get(resource));
        }
    }
}
