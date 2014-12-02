package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class HotMusic {
    Integer id;
    String title;
    String music_url;
    Long update_timestamp;

    public HotMusic(Integer id, String title, String music_url, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.music_url = music_url;
        this.update_timestamp = update_timestamp;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
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

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
