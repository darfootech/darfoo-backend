package com.springapp.mvc.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.ImageDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;
import com.qiniu.api.rsf.RSFEofException;
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

//根据服务器上数据库中资源key来删除七牛云上没有用的资源，节省空间，方便管理资源
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DeleteTrashResource {
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommonDao commonDao;

    public List<String> getFileList() {
        Config.ACCESS_KEY = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        Config.SECRET_KEY = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
        String bucketName = "zjdxlab410yy";
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);

        RSFClient client = new RSFClient(mac);
        String marker = "";

        List<ListItem> all = new ArrayList<ListItem>();
        ListPrefixRet ret = null;
        while (true) {
            ret = client.listPrifix(bucketName, "", marker, 1000);
            marker = ret.marker;
            all.addAll(ret.results);
            if (!ret.ok()) {
                // no more items or error occurs
                break;
            }
        }
        if (ret.exception.getClass() != RSFEofException.class) {
            // error handler
        }

        List<String> remoteList = new ArrayList<String>();
        for (ListItem item : all) {
            System.out.println(item.key);
            remoteList.add(item.key);
        }
        return remoteList;
    }

    public void deleteResource(String key) {
        Config.ACCESS_KEY = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        Config.SECRET_KEY = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
        String bucketName = "zjdxlab410yy";
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        RSClient client = new RSClient(mac);
        client.delete(bucketName, key);
    }

    @Test
    public void deleteTrashResource() {
        List<String> keyList = new ArrayList<String>();

        List<Video> videoList = commonDao.getAllResource(Video.class);
        List<Tutorial> tutorialList = commonDao.getAllResource(Tutorial.class);
        List<Music> musicList = commonDao.getAllResource(Music.class);
        List<Image> imageList = commonDao.getAllResource(Image.class);

        for (Video video : videoList) {
            keyList.add(video.getVideo_key());
        }

        for (Tutorial tutorial : tutorialList) {
            keyList.add(tutorial.getVideo_key());
        }

        for (Music music : musicList) {
            keyList.add(music.getMusic_key() + ".mp3");
        }

        for (Image image : imageList) {
            keyList.add(image.getImage_key());
        }

        List<String> remoteList = getFileList();
        for (String key : remoteList) {
            if (!keyList.contains(key)) {
                deleteResource(key);
            }
        }
    }
}
