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
        System.out.println("download url -> " + qiniuUtils.getQiniuResourceUrl("留下来.mp4", QiniuResourceEnum.RAWNORMAL));
    }
}
