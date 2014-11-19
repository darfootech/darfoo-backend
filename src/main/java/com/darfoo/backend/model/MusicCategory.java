package com.darfoo.backend.model;

import java.io.Serializable;

/**
 * Created by zjh on 14-11-16.
 */
public class MusicCategory implements Serializable {
    Integer id;
    String title;
    String description;

    Music[] musics;

    public MusicCategory() {
        this.id = 333;
        this.title = "music-cate";
        this.description = "music category";
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

    public Music[] getMusics() {
        return musics;
    }

    public void setMusics(Music[] musics) {
        this.musics = musics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
