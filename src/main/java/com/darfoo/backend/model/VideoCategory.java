package com.darfoo.backend.model;

import java.io.Serializable;

/**
 * Created by zjh on 14-11-16.
 */
public class VideoCategory implements Serializable {
    Integer id;
    String title;
    String description;

    Video[] videos;

    public VideoCategory() {
        this.id = 3;
        this.title = "dance";
        this.description = "let's play dance";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Video[] getVideos() {
        return videos;
    }

    public void setVideos(Video[] videos) {
        this.videos = videos;
    }
}
