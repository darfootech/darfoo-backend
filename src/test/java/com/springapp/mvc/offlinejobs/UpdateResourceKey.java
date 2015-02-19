package com.springapp.mvc.offlinejobs;

import com.darfoo.backend.dao.CommonDao;
import com.darfoo.backend.dao.TutorialDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
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
    VideoDao videoDao;
    @Autowired
    TutorialDao educationDao;
    @Autowired
    CommonDao commonDao;

    @Test
    public void updateVideoKey() {
        List<Video> s_videos = commonDao.getAllResource(Video.class);
        for (Video video : s_videos) {
            int videoid = video.getId();
            String videokey = video.getVideo_key() + ".mp4";
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("video_key", videokey);
            commonDao.updateResourceFieldsById(Video.class, videoid, updateMap);
        }
        System.out.println("总共查到" + s_videos.size());
    }

    @Test
    public void updateTutorialKey() {
        List<Tutorial> s_educations = commonDao.getAllResource(Tutorial.class);
        for (Tutorial tutorial : s_educations) {
            int tutorialid = tutorial.getId();
            String tutorialkey = tutorial.getVideo_key() + ".mp4";
            educationDao.updateVideoKeyById(tutorialid, tutorialkey);
        }
        System.out.println("总共查到" + s_educations.size());
    }

}
