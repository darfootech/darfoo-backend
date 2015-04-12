package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 15-4-12.
 */
public class SingleOperaSeries {
    Integer id;
    String title;
    String image_url;

    public SingleOperaSeries() {
    }

    public SingleOperaSeries(Integer id, String title, String image_url) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "SingleOperaSeries{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
