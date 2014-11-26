package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SingleMusic {
    Integer id;
    String music_url;
    String title;

    public SingleMusic(Integer id, String music_url, String title) {
        this.id = id;
        this.music_url = music_url;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
