package com.darfoo.backend.utils;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;

/**
 * Created by zjh on 14-11-26.
 */
public class QiniuUtils {
    public QiniuUtils() {
        Config.ACCESS_KEY = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        Config.SECRET_KEY = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
    }

    //key就是七牛云上的文件名字
    public String getQiniuResourceUrl(String key) {
        String domain = "zjdxlab410yy.qiniudn.com";
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        try {
            //domain在空间设置里可以看到，每一个bucket都对应有一个域名
            //所谓的key其实就是上传的文件名字
            String baseUrl = URLUtils.makeBaseUrl(domain, key);
            GetPolicy getPolicy = new GetPolicy();
            //过期时间为一周
            getPolicy.expires = 7 * 24 * 3600;
            String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
            System.out.println(downloadUrl);
            return downloadUrl;
        } catch (Exception e) {
            //return "generate download url error";
            return e.getMessage();
        }
    }

    public String uploadResouce(String fileLocation, String fileName) {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
        String bucketName = "zjdxlab410yy";
        PutPolicy putPolicy = new PutPolicy(bucketName);
        try {
            String uptoken = putPolicy.token(mac);
            PutExtra extra = new PutExtra();
            String key = fileName;
            String localFile = fileLocation;
            PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
            return ret.getStatusCode() + "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
