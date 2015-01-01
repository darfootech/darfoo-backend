package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SearchMusic {
    Integer id;
    String title;
    //String image_url;
    String music_url;
    String authorname;
    Long update_timestamp;

    /*public SearchMusic(Integer id, String title, String image_url, String music_url, String authorname, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.music_url = music_url;
        this.authorname = authorname;
        this.update_timestamp = update_timestamp;
    }*/

    public SearchMusic(Integer id, String title, String music_url, String authorname, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.music_url = music_url;
        this.authorname = authorname;
        this.update_timestamp = update_timestamp;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    /*public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }*/

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
