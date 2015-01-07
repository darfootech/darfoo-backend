package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-25.
 * 获取单个视频信息
 */
public class SingleVideo {
    Integer id;
    String title;
    String authorname;
    String video_url;
    String image_url;
    Long update_timestamp;

    public SingleVideo(Integer id, String title, String authorname, String video_url, String image_url, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.authorname = authorname;
        this.video_url = video_url;
        this.image_url = image_url;
        this.update_timestamp = update_timestamp;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
