package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-11-26.
 */
public class CategoryMusic {
    Integer id;
    String image_url;
    String title;
    Long update_timestamp;

    public CategoryMusic(Integer id, String image_url, String title, Long update_timestamp) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.update_timestamp = update_timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
