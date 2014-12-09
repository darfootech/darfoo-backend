package com.springapp.mvc.offlinejobs;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
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
    EducationDao educationDao;

    @Test
    public void updateVideoKey(){
        List<Video> s_videos;
        s_videos = videoDao.getAllVideo();
        for(Video video : s_videos){
            int videoid = video.getId();
            String videokey = video.getVideo_key() + ".mp4";
            videoDao.updateVideoKeyById(videoid, videokey);
        }
        System.out.println("总共查到"+s_videos.size());
    }

    @Test
    public void updateTutorialKey(){
        List<Education> s_educations;
        s_educations = educationDao.getAllEdutcaion();
        for(Education tutorial : s_educations){
            int tutorialid = tutorial.getId();
            String tutorialkey = tutorial.getVideo_key() + ".mp4";
            educationDao.updateVideoKeyById(tutorialid, tutorialkey);
        }
        System.out.println("总共查到"+s_educations.size());
    }

}
