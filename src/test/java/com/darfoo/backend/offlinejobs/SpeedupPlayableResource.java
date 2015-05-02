package com.darfoo.backend.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-5-1.
 */

//使用视频切片来加速视频加载速度
//主要是视频资源和音频资源
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class SpeedupPlayableResource {
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;

    @Test
    public void speedupResources() {
        List<String> resourcekeys = new ArrayList<String>();
        List dancevideos = commonDao.getAllResource(DanceVideo.class);
        List dancemusics = commonDao.getAllResource(DanceMusic.class);
        List operavideos = commonDao.getAllResource(OperaVideo.class);

        for (Object danceVideo : dancevideos) {
            resourcekeys.add((String) commonDao.getResourceAttr(DanceVideo.class, danceVideo, "video_key"));
        }

        for (Object danceMusic : dancemusics) {
            resourcekeys.add((String) commonDao.getResourceAttr(DanceMusic.class, danceMusic, "music_key"));
        }

        for (Object operaVideo : operavideos) {
            resourcekeys.add((String) commonDao.getResourceAttr(OperaVideo.class, operaVideo, "video_key"));
        }

        for (String key : resourcekeys) {
            System.out.println(key);
            qiniuUtils.resourceOperation(key);
        }

        System.out.println("total playable resource -> " + resourcekeys.size());
    }

    @Test
    public void batchChangeDanceVideoKey() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("speedup_key", "");
        List dancevideos = commonDao.getResourcesByFields(DanceVideo.class, conditions);
        HashMap<String, Object> updateConditions = new HashMap<String, Object>();

        for (Object danceVideo : dancevideos) {
            String videokey = (String) commonDao.getResourceAttr(DanceVideo.class, danceVideo, "video_key");
            Integer id = (Integer) commonDao.getResourceAttr(DanceVideo.class, danceVideo, "id");
            System.out.println("originkey -> " + videokey);
            System.out.println("id -> " + id);
            String newkey = videokey.replaceAll("-\\d+", String.format("-%d", id));
            System.out.println("newkey -> " + newkey);
            updateConditions.put("video_key", newkey);

            commonDao.updateResourceFieldsById(DanceVideo.class, id, updateConditions);
            qiniuUtils.renameFile(videokey, newkey);
        }
    }

    @Test
    public void speedupResourcesWithFailedRecords() {
        qiniuUtils.speedupResources();
    }
}
