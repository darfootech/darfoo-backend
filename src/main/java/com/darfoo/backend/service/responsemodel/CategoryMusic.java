package com.darfoo.backend.service.responsemodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-11-26.
 */
public class CategoryMusic {
    Integer id;
    //String image_url;
    String music_url;
    String title;
    String authorname;
    Long update_timestamp;

    /*public CategoryMusic(Integer id, String image_url, String music_url, String title, String authorname, Long update_timestamp) {
        this.id = id;
        this.image_url = image_url;
        this.music_url = music_url;
        this.title = title;
        this.authorname = authorname;
        this.update_timestamp = update_timestamp;
    }*/

    public CategoryMusic(Integer id, String music_url, String title, String authorname, Long update_timestamp) {
        this.id = id;
        this.music_url = music_url;
        this.title = title;
        this.authorname = authorname;
        this.update_timestamp = update_timestamp;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
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

    /*public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }*/

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
