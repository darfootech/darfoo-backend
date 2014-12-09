package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-12-9.
 */
public class SingleAuthor {
    Integer id;
    String image_url;
    String title;
    String description;

    public SingleAuthor(Integer id, String image_url, String title, String description) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
