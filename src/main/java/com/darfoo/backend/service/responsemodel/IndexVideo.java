package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-25.
 */
public class IndexVideo {
    Integer id;
    String title;
    String image_url;
    Long update_timestamp;

    public IndexVideo(Integer id, String title, String image_url, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.update_timestamp = update_timestamp;
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
}
