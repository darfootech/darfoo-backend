package com.darfoo.backend;

/**
 * Created by zjh on 14-12-11.
 */

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.annotations.CSVTitle;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.statistics.CrashLog;
import com.darfoo.backend.model.statistics.SearchHistory;
import com.darfoo.backend.model.statistics.clickcount.MenuClickCount;
import com.darfoo.backend.model.statistics.clickcount.ResourceClickCount;
import com.darfoo.backend.model.statistics.clickcount.TabClickCount;
import com.darfoo.backend.model.statistics.clicktime.MenuClickTime;
import com.darfoo.backend.model.statistics.clicktime.ResourceClickTime;
import com.darfoo.backend.model.statistics.clicktime.TabClickTime;
import com.darfoo.backend.service.category.DanceMusicCates;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.utils.DiskFileDirConfig;
import com.darfoo.backend.utils.DownloadUtils;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class DownloadTests {
    @Autowired
    DownloadUtils downloadUtils;

    @Test
    public void writeResourcesToCSV() {
        Class[] classes = {DanceVideo.class, DanceMusic.class, DanceGroup.class};
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
                SearchHistory.class
        };
        for (Class c : classes) {
            downloadUtils.writeStatisticDataToCSV(c);
        }
    }

    @Test
    public void writeVideosOfAuthorToCSV() {
        Integer id = 109;
        downloadUtils.writeVideosOfDanceGroupToCSV(id);
    }
}
