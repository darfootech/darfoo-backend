package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 15-4-12.
 */
public class SingleOperaVideo {
    Integer id;
    String title;
    String seriesname;
    String video_url;
    String image_url;
    Long update_timestamp;

    public SingleOperaVideo() {
    }

    public SingleOperaVideo(Integer id, String title, String seriesname, String video_url, String image_url, Long update_timestamp) {
        this.id = id;
        this.title = title;
        this.seriesname = seriesname;
        this.video_url = video_url;
        this.image_url = image_url;
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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getSeriesname() {
        return seriesname;
    }

    public void setSeriesname(String seriesname) {
        this.seriesname = seriesname;
    }

    @Override
    public String toString() {
        return "SingleOperaVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", seriesname='" + seriesname + '\'' +
                ", video_url='" + video_url + '\'' +
                ", image_url='" + image_url + '\'' +
                ", update_timestamp=" + update_timestamp +
                '}';
    }
}
