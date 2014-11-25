package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-25.
 */
public class RecommendVideo {
    Integer id;
    String title;
    String video_url;

    public RecommendVideo(Integer id, String title, String video_url) {
        this.id = id;
        this.title = title;
        this.video_url = video_url;
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
