package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SingleMusic {
    Integer id;
    //String image_url;
    String title;
    String authorname;
    String music_url;
    Long update_timestamp;

    /*public SingleMusic(Integer id, String music_url, String image_url, String authorname, String title) {
        this.id = id;
        this.music_url = music_url;
        this.image_url = image_url;
        this.authorname = authorname;
        this.title = title;
    }*/

    public SingleMusic(Integer id, String title, String authorname, String music_url, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.authorname = authorname;
        this.music_url = music_url;
        this.update_timestamp = update_timestamp / 1000;
    }

    /*public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }*/

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
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
