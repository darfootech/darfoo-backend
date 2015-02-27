package com.darfoo.backend.model.upload;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-1-17.
 */

@Entity
@Table(name = "uploadnoauthvideo")
public class UploadNoAuthVideo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //引用下面名为mysql的主键生成方式
    Integer id;

    //视频资源在七牛上的标识
    @Column(name = "VIDEO_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String video_key;

    //图片资源在七牛上的标识
    @Column(name = "IMAGE_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String image_key;

    //上传机器的mac地址
    @Column(name = "MAC_ADDR", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String mac_addr;

    //上传视频的标题
    @Column(name = "VIDEO_TITLE", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String title;

    //上传视频的类型
    @Column(name = "VIDEO_TYPE", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String videotype;

    public UploadNoAuthVideo() {
    }

    public UploadNoAuthVideo(String video_key, String image_key, String mac_addr, String title, String videotype) {
        this.video_key = video_key;
        this.image_key = image_key;
        this.mac_addr = mac_addr;
        this.title = title;
        this.videotype = videotype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getMac_addr() {
        return mac_addr;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }

    public String getVideotype() {
        return videotype;
    }

    public void setVideotype(String videotype) {
        this.videotype = videotype;
    }
}
