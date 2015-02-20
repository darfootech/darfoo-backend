package com.darfoo.backend.model.upload;

/**
 * Created by zjh on 15-1-11.
 */

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户自己上传的视频
 */

@Entity
@Table(name = "uploadvideo")
public class UploadVideo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //引用下面名为mysql的主键生成方式
            Integer id;

    //视频资源在七牛上的标识
    @Column(name = "VIDEO_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String video_key;

    //上传用户的id
    @Column(name = "USER_ID", nullable = false, updatable = true, columnDefinition = "int(11) default 0")
    Integer userid;

    //对应最后真正到视频库里的视频id
    @Column(name = "VIDEO_ID", nullable = false, updatable = true, columnDefinition = "int(11) default 0")
    Integer videoid;

    public UploadVideo() {
    }

    public UploadVideo(String video_key, Integer userid, Integer videoid) {
        this.video_key = video_key;
        this.userid = userid;
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getVideoid() {
        return videoid;
    }

    public void setVideoid(Integer videoid) {
        this.videoid = videoid;
    }
}
