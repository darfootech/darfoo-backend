package com.darfoo.backend.upload;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UploadNoAuthVideoDaoTests {
    @Autowired
    CommonDao commonDao;

    @Test
    public void isExistVideo() {
        String videokey = "cleantha33-1425022872317-30:ad:05:01:a6:83.mp4";
        System.out.println("is exists -> " + commonDao.isResourceExistsByField(UploadNoAuthVideo.class, "video_key", videokey));
    }

    @Test
    public void insertUploadVideo() {
        String videokey = "cleantha33-" + System.currentTimeMillis() + "-30:ad:05:01:a6:83.mp4";
        String macaddr = videokey.split("\\.")[0].split("-")[2];
        String videoTitle = videokey.split("\\.")[0].split("-")[0];

        HashMap<String, String> insertcontents = new HashMap<String, String>();

        insertcontents.put("title", videoTitle);
        insertcontents.put("video_key", videokey);
        insertcontents.put("macaddr", macaddr);

        HashMap<String, Integer> insertresult = commonDao.insertResource(UploadNoAuthVideo.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
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
