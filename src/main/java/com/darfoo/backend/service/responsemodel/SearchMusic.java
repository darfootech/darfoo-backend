package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SearchMusic {
    Integer id;
    String title;
    Long update_timestamp;

    public SearchMusic(Integer id, String title, Long update_timestamp) {
        this.id = id;
        this.title = title;
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

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
