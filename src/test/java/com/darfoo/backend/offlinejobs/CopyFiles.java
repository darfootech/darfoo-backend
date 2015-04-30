package com.darfoo.backend.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.Image;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-4-29.
 */

//将图片从旧的bucket中复制到新的bucket中
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class CopyFiles {
    @Autowired
    CommonDao commonDao;

    public void copyFile(BucketManager bucketManager, String originbucket, String originkey, String targetbucket, String targetkey) {
        try {
            bucketManager.copy(originbucket, originkey, targetbucket, targetkey);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteAllFiles() {
        String accesskey = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        String secretkey = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
        Auth auth = Auth.create(accesskey, secretkey);
        BucketManager bucketManager = new BucketManager(auth);
        String bucket = "cleantha";

        BucketManager.FileListIterator it = bucketManager.createFileListIterator(bucket, "", 1000, null);

        while (it.hasNext()) {
            FileInfo[] items = it.next();
            if (items.length > 0) {
                for (FileInfo fileInfo : items) {
                    String key = fileInfo.key;
                    System.out.println(key);
                    try {
                        bucketManager.delete(bucket, key);
                    } catch (QiniuException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Test
    public void copyImageFiles() {
        String accesskey = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        String secretkey = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
        Auth auth = Auth.create(accesskey, secretkey);
        BucketManager bucketManager = new BucketManager(auth);

        String originbucket = "zjdxlab410yy";
        String targetbucket = "cleantha";

        Class resource = Image.class;
        List images = commonDao.getAllResource(resource);
        for (Object image : images) {
            String imagekey = (String) commonDao.getResourceAttr(resource, image, "image_key");
            System.out.println(imagekey);
            copyFile(bucketManager, originbucket, imagekey, targetbucket, imagekey);
        }
    }
}
