package com.springapp.mvc.upload;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.upload.UploadNoAuthVideoDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
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
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonDao commonDao;

    @Test
    public void isExistVideo() {
        //String videokey = "cleantha33-1421475655469-30:ad:05:01:a6:83.mp4";
        String videokey = "cleantha";
        System.out.println("is exists -> " + uploadVideoDao.isExistVideo(videokey));
    }

    @Test
    public void insertUploadVideo() {
        String videokey = "cleantha33-" + System.currentTimeMillis() + "-30:ad:05:01:a6:83.mp4";
        String imagekey = "cleantha0-" + System.currentTimeMillis() + "-30:ad:05:01:a6:83.mp3";
        System.out.println(videokey);
        int videoid = -1;
        String macaddr = videokey.split("\\.")[0].split("-")[2];
        String videotitle = videokey.split("\\.")[0].split("-")[0];
        String videotype = videokey.split("\\.")[1];

        int result = uploadVideoDao.insertUploadVideo(new UploadNoAuthVideo(videokey, imagekey, macaddr, videotitle, videotype, videoid));
        Assert.assertEquals(CRUDEvent.INSERT_SUCCESS, result);
    }

    @Test
    public void getUploadVideoById() {
        int id = 1;
        UploadNoAuthVideo video = (UploadNoAuthVideo) commonDao.getResourceById(UploadNoAuthVideo.class, id);
        System.out.println(video.getVideo_key());
    }

    @Test
    public void getAllVideo() {
        List<UploadNoAuthVideo> videos = commonDao.getAllResource(UploadNoAuthVideo.class);
        for (UploadNoAuthVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void deleteVideoById() {
        int id = 1;
        int result = commonDao.deleteResourceById(UploadNoAuthVideo.class, id);
        Assert.assertEquals(CRUDEvent.DELETE_SUCCESS, result);
    }
}
