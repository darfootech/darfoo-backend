package com.springapp.mvc;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class UploadNoAuthVideoDaoTests {

    @Autowired
    UploadNoAuthVideoDao uploadVideoDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    VideoDao videoDao;
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
    public void updateRealVideoId() {
        int id = 2;
        int videoid = 6;
        int result = uploadVideoDao.updateRealVideoid(id, videoid);
        Assert.assertEquals(CRUDEvent.UPDATE_SUCCESS, result);
    }

    @Test
    public void getUploadVideoById() {
        int id = 1;
        UploadNoAuthVideo video = uploadVideoDao.getUploadVideoById(id);
        System.out.println(video.getVideo_key());
    }

    @Test
    public void getAllVideo() {
        List<UploadNoAuthVideo> videos = uploadVideoDao.getAllVideo();
        for (UploadNoAuthVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void getAllUnVerifyVideos() {
        List<UploadNoAuthVideo> videos = uploadVideoDao.getAllUnVerifyVideos();
        for (UploadNoAuthVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void getVideosByMacAddr() {
        String macaddr = "30:ad:05:01:a6:83";
        List<UploadNoAuthVideo> videos = uploadVideoDao.getVideosByMacAddr(macaddr);
        for (UploadNoAuthVideo video : videos) {
            System.out.println(video.getVideo_key());
        }
    }

    @Test
    public void deleteVideoById() {
        int id = 1;
        int result = uploadVideoDao.deleteVideoById(id);
        Assert.assertEquals(CRUDEvent.DELETE_SUCCESS, result);
    }

    @Test
    public void insertSingleVideo() {
        String videotitle = "cleantha33";
        String imagekey = "cleantha0-1421489883812-30:ad:05:01:a6:83.mp3";
        String authorname = "user-macaddress";
        String videotype = "mp4";
        String videospeed = "较快";
        String videodifficult = "简单";
        String videostyle = "欢快";
        String videoletter = "A";

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isSingleLetter = ServiceUtils.isSingleCharacter(videoletter);
        if (isSingleLetter) {
            System.out.println("是单个大写字母");
        } else {
            System.out.println("不是单个大写字母");
            resultMap.put("statuscode", 505);
            resultMap.put("insertid", -1);
        }

        if (imagekey.equals("")) {
            resultMap.put("statuscode", 508);
            resultMap.put("insertid", -1);
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            resultMap.put("statuscode", 502);
            resultMap.put("insertid", -1);
        }

        Author targetAuthor = (Author) commonDao.getResourceByTitleOrName(Author.class, authorname, "name");
        if (targetAuthor != null) {
            System.out.println(targetAuthor.getName());
        } else {
            targetAuthor = new Author();
            targetAuthor.setName(authorname);
            targetAuthor.setDescription("userdescription");
            targetAuthor.setImage(image);
            authorDao.insertAuthor(targetAuthor);
        }

        Video video = new Video();
        video.setAuthor(targetAuthor);
        Image img = new Image();
        img.setImage_key(imagekey);
        video.setImage(img);
        VideoCategory speed = new VideoCategory();
        VideoCategory difficult = new VideoCategory();
        VideoCategory style = new VideoCategory();
        VideoCategory letter = new VideoCategory();
        speed.setTitle(videospeed);
        difficult.setTitle(videodifficult);
        style.setTitle(videostyle);
        letter.setTitle(videoletter);
        Set<VideoCategory> s_vCategory = video.getCategories();
        s_vCategory.add(speed);
        s_vCategory.add(difficult);
        s_vCategory.add(style);
        s_vCategory.add(letter);
        video.setTitle(videotitle);
        video.setVideo_key(videotitle);
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = videoDao.insertSingleVideo(video);
        if (insertStatus == -1) {
            System.out.println("插入视频失败");
        } else {
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", videotitle + "-" + insertStatus + "." + videotype);
        commonDao.updateResourceFieldsById(Video.class, insertStatus, updateMap);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertStatus);
        System.out.println(resultMap.get("statuscode"));
    }
}
