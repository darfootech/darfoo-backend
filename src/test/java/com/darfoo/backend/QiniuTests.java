package com.darfoo.backend;

import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-4-29.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class QiniuTests {
    @Autowired
    QiniuUtils qiniuUtils;

    @Test
    public void getDownloadUrl() {
        String normalkey = "鼓楼天歌-口令 背面-dancevideo-1705.mp4";
        //String m3u8key = "a99slR6hWZjQWtJUbE8Ze6SdRyU=/ljAnjyjYJYJFi5NB6dSNgk1Xd1Qs";
        String m3u8key = "a99slR6hWZjQWtJUbE8Ze6SdRyU=/FhAgoIWHUOmEdDT0kDWi4K-jJSGu";
        //String normalkey = "东方姑娘-dancevideo-1279.flv";
        //String m3u8key = "a99slR6hWZjQWtJUbE8Ze6SdRyU=/lkZ7lFcHqt-wkZGeTGLTSlTJvBp_";
        System.out.println("normal download url -> " + qiniuUtils.getQiniuResourceUrl(normalkey, QiniuResourceEnum.RAWNORMAL));
        System.out.println("m3u8 download url -> " + qiniuUtils.getQiniuResourceUrl(m3u8key, QiniuResourceEnum.M3U8));
    }

    @Test
    public void renameFile() {
        qiniuUtils.renameFile("留下来.mp4", "留下来吧33.mp4");
    }

    @Test
    public void generateM3U8PlayableFile() {
        //String key = "鼓楼天歌-口令 背面-dancevideo-1705.mp4";
        //String key = "东方姑娘-dancevideo-1279.flv";
        String key = "磕儿-169.mp3";
        qiniuUtils.resourceOperation(key);
    }
}
