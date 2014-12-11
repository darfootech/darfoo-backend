package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-25.
 * 出现在首页上的视频信息，包括recommend和latest
 */
public class CategoryVideo {
    Integer id;
    String title;
    String author_name;
    String image_url;
    String video_url;
    Long update_timestamp;

    public CategoryVideo(Integer id, String title, String author_name, String image_url, String video_url, Long update_timestamp) {
        this.id = id;
        this.title = title + " - " + author_name;
        this.author_name = author_name;
        this.image_url = image_url;
        this.video_url = video_url;
        this.update_timestamp = update_timestamp;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
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

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
