package com.springapp.mvc.upload;

/**
 * Created by zjh on 15-1-11.
 */

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.upload.UploadVideoDao;
import com.darfoo.backend.model.upload.UploadVideo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UploadVideoDaoTests {
    @Autowired
    UploadVideoDao uploadVideoDao;

    @Test
    public void isExistVideo() {
        String videokey = "cleantha";
        System.out.println("is exists -> " + uploadVideoDao.isExistVideo(videokey));
    }

    @Test
    public void insertUploadVideo() {
        int userid = 3;
        int videoid = -1;
        String videokey = "cleantha33";
        int result = uploadVideoDao.insertUploadVideo(new UploadVideo(videokey, userid, videoid));
        Assert.assertEquals(CRUDEvent.INSERT_SUCCESS, result);
        /*if (result == CRUDEvent.INSERT_SUCCESS){
            System.out.println("insert success");
        }else{
            System.out.println("insert failed");
        }*/
    }

    @Test
    public void updateRealVideoId() {
        int id = 1;
        int videoid = 3;
        int result = uploadVideoDao.updateRealVideoid(id, videoid);
        Assert.assertEquals(CRUDEvent.UPDATE_SUCCESS, result);
    }

    @Test
    public void getUploadVideoById() {
        int id = 1;
        UploadVideo video = uploadVideoDao.getUploadVideoById(id);
        System.out.println(video.getVideo_key());
    }

    @Test
    public void getAllVideo() {
        List<UploadVideo> videos = uploadVideoDao.getAllVideo();
        for (UploadVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void getVideosByUserId() {
        int userid = 3;
        List<UploadVideo> videos = uploadVideoDao.getVideosByUserId(userid);
        for (UploadVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void deleteVideoById() {
        int id = 4;
        int result = uploadVideoDao.deleteVideoById(id);
        Assert.assertEquals(CRUDEvent.DELETE_SUCCESS, result);
    }
}
