package com.darfoo.backend.utils;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.resumableio.ResumeableIoApi;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rs.URLUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by zjh on 14-11-26.
 */
public class QiniuUtils {
    private String bucketName;
    private String mimeType;
    private String domain;

    public QiniuUtils() {
        Config.ACCESS_KEY = "bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X";
        Config.SECRET_KEY = "eMZK5q9HI1EXe7KzNtsyKJZJPHEfh96XcHvDigyG";
        this.bucketName = "zjdxlab410yy";
        this.mimeType = null;
        //this.domain = "zjdxlab410yy.qiniudn.com"; // godaddy的一次dns问题
        this.domain = "7qnarb.com1.z0.glb.clouddn.com";
    }

    public String getToken() {
        PutPolicy policy = new PutPolicy(bucketName);
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        try {
            String upToken = policy.token(mac);
            return upToken;
        } catch (AuthException e) {
            e.printStackTrace();
            return "error";
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    //key就是七牛云上的文件名字
    //根据type来判断资源链接是否需要加密 暂时图片资源链接不需要加密
    public String getQiniuResourceUrl(String key, QiniuResourceEnum type) {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        try {
            //domain在空间设置里可以看到，每一个bucket都对应有一个域名
            //所谓的key其实就是上传的文件名字
            String baseUrl = URLUtils.makeBaseUrl(domain, key);
            GetPolicy getPolicy = new GetPolicy();
            //过期时间为一周
            getPolicy.expires = 7 * 24 * 3600;
            if (type == QiniuResourceEnum.ENCRYPT) {
                System.out.println(baseUrl);
                String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
                return CryptUtils.encryptQiniuUrl(downloadUrl);
            } else if (type == QiniuResourceEnum.RAW) {
                //缩小图片
                if (key.contains("recommend")) {
                    System.out.println(baseUrl);
                    return getPolicy.makeRequest(baseUrl, mac);
                }else {
                    String baseUrlSmall = String.format("%s?imageView2/2/w/230/h/126", baseUrl);
                    System.out.println(baseUrlSmall);
                    return getPolicy.makeRequest(baseUrlSmall, mac);
                }
            } else {
                System.out.println("wired");
                return "";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    //尝试使用断点续传和并行分块上传
    public String uploadResourceStream(String fileLocation, String fileName) {
        System.out.println("start to upload resource to qiniu server");
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        PutPolicy putPolicy = new PutPolicy(this.bucketName);

        try {
            String upToken = putPolicy.token(mac);
            String key = fileName;
            FileInputStream fis = new FileInputStream(new File(fileLocation));
            PutRet ret = ResumeableIoApi.put(fis, upToken, key, this.mimeType);
            return ret.getStatusCode() + "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void deleteResource(String key) {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        RSClient client = new RSClient(mac);
        client.delete(bucketName, key);
    }
}
