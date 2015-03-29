package com.darfoo.backend.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 14-12-9.
 */

//现在视频资源支持mp4和flv两种格式，所以需要把视频格式直接写在key中，所以需要更新之前数据库中的key
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UpdateResourceKey {
    @Autowired
    CommonDao commonDao;

    @Test
    public void updateVideoKey() {
        List<DanceVideo> s_videos = commonDao.getAllResource(DanceVideo.class);
        for (DanceVideo video : s_videos) {
            int videoid = video.getId();
            String videokey = video.getVideo_key() + ".mp4";
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("video_key", videokey);
            commonDao.updateResourceFieldsById(DanceVideo.class, videoid, updateMap);
        }
        System.out.println("总共查到" + s_videos.size());
    }
}
