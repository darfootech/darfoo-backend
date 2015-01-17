package com.darfoo.backend.model;

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

    //上传用户的id
    @Column(name = "MAC_ADDR", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String mac_addr;

    //对应最后真正到视频库里的视频id
    @Column(name = "VIDEO_ID", nullable = false, updatable = true, columnDefinition = "int(11) default 0")
    Integer videoid;

    public UploadNoAuthVideo() {
    }

    public UploadNoAuthVideo(String video_key, String mac_addr, Integer videoid) {
        this.video_key = video_key;
        this.mac_addr = mac_addr;
        this.videoid = videoid;
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

    public Integer getVideoid() {
        return videoid;
    }

    public void setVideoid(Integer videoid) {
        this.videoid = videoid;
    }

    public String getMac_addr() {
        return mac_addr;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }
}
