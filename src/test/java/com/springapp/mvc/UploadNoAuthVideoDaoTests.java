package com.springapp.mvc;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.UploadNoAuthVideoDao;
import com.darfoo.backend.model.UploadNoAuthVideo;
import com.darfoo.backend.model.UploadVideo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UploadNoAuthVideoDaoTests {

    @Autowired
    UploadNoAuthVideoDao uploadVideoDao;

    @Test
    public void isExistVideo(){
        //String videokey = "cleantha33-1421475655469-30:ad:05:01:a6:83.mp4";
        String videokey = "cleantha";
        System.out.println("is exists -> " + uploadVideoDao.isExistVideo(videokey));
    }

    @Test
    public void insertUploadVideo(){
        String videokey = "cleantha33-" + System.currentTimeMillis() + "-30:ad:05:01:a6:83.mp4";
        System.out.println(videokey);
        int videoid = -1;
        String macaddr = videokey.split("\\.")[0].split("-")[2];
        int result = uploadVideoDao.insertUploadVideo(new UploadNoAuthVideo(videokey, macaddr, videoid));
        Assert.assertEquals(CRUDEvent.INSERT_SUCCESS, result);
    }

    @Test
    public void updateRealVideoId(){
        int id = 2;
        int videoid = 6;
        int result = uploadVideoDao.updateRealVideoid(id, videoid);
        Assert.assertEquals(CRUDEvent.UPDATE_SUCCESS, result);
    }

    @Test
    public void getUploadVideoById(){
        int id = 1;
        UploadNoAuthVideo video = uploadVideoDao.getUploadVideoById(id);
        System.out.println(video.getVideo_key());
    }

    @Test
    public void getAllVideo(){
        List<UploadNoAuthVideo> videos = uploadVideoDao.getAllVideo();
        for (UploadNoAuthVideo video : videos){
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void getAllUnVerifyVideos(){
        List<UploadNoAuthVideo> videos = uploadVideoDao.getAllUnVerifyVideos();
        for (UploadNoAuthVideo video : videos){
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void getVideosByMacAddr(){
        String macaddr = "30:ad:05:01:a6:83";
        List<UploadNoAuthVideo> videos = uploadVideoDao.getVideosByMacAddr(macaddr);
        for (UploadNoAuthVideo video : videos){
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void deleteVideoById(){
        int id = 1;
        int result = uploadVideoDao.deleteVideoById(id);
        Assert.assertEquals(CRUDEvent.DELETE_SUCCESS, result);
    }
}
