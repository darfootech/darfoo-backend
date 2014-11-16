package com.darfoo.backend.model;

/**
 * Created by zjh on 14-11-16.
 */
public class Music {
    Integer id;
    Integer category_id;
    Integer author_id;

    String title;
    String music_key;
    Long update_timestamp;

    public Music() {
        this.id = 3;
        this.category_id = 33;
        this.author_id = 333;
        this.title = "music";
        this.music_key = "music_key";
        this.update_timestamp = 1416121149L;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusic_key() {
        return music_key;
    }

    public void setMusic_key(String music_key) {
        this.music_key = music_key;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
