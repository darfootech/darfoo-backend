package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SingleMusic {
    Integer id;
    String music_url;
    //String image_url;
    String authorname;
    String title;

    /*public SingleMusic(Integer id, String music_url, String image_url, String authorname, String title) {
        this.id = id;
        this.music_url = music_url;
        this.image_url = image_url;
        this.authorname = authorname;
        this.title = title;
    }*/

    public SingleMusic(Integer id, String music_url, String authorname, String title) {
        this.id = id;
        this.music_url = music_url;
        this.authorname = authorname;
        this.title = title;
    }

    /*public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }*/

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
