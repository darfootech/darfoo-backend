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

    //上传视频的标题
    @Column(name = "VIDEO_TITLE", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String title;

    //视频资源在七牛上的标识
    @Column(name = "VIDEO_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String video_key;

    //上传机器的mac地址
    @Column(name = "MAC_ADDR", nullable = false, updatable = true, columnDefinition = "varchar(255) not null")
    String macaddr;

    public UploadNoAuthVideo() {
    }

    public UploadNoAuthVideo(String title, String video_key, String macaddr) {
        this.title = title;
        this.video_key = video_key;
        this.macaddr = macaddr;
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

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
