package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-11-26.
 */
public class SearchVideo {
    Integer id;
    String title;
    String image_url;
    String video_url;
    String author_name;
    Long update_timestamp;

    public SearchVideo(Integer id, String title, String image_url, String video_url, String authorname, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.video_url = video_url;
        this.author_name = authorname;
        this.update_timestamp = update_timestamp;
    }

    public String getAuthorname() {
        return author_name;
    }

    public void setAuthorname(String authorname) {
        this.author_name = authorname;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
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

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
