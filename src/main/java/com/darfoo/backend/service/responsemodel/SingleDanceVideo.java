package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 15-1-13.
 */

//-> 替换之前的singlevideo加上了判别是欣赏视频还是教学视频的字段
public class SingleDanceVideo {
    Integer id;
    String title;
    String authorname;
    String video_url;
    String image_url;
    //-> 教学是0 欣赏时1
    Integer type;
    Long update_timestamp;

    public SingleDanceVideo() {
    }

    public SingleDanceVideo(Integer id, String title, String authorname, String video_url, String image_url, Integer type, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.authorname = authorname;
        this.video_url = video_url;
        this.image_url = image_url;
        this.type = type;
        this.update_timestamp = update_timestamp;
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

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SingleVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorname='" + authorname + '\'' +
                ", video_url='" + video_url + '\'' +
                ", image_url='" + image_url + '\'' +
                ", type=" + type +
                ", update_timestamp=" + update_timestamp +
                '}';
    }
}
