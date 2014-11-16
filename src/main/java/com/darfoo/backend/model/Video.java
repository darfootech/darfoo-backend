package com.darfoo.backend.model;

/**
 * Created by zjh on 14-11-16.
 */
public class Video {
    /*keys*/
    Integer id;
    //Integer category_id;
    Integer author_id;
    Integer image_id;

    /*info*/
    String video_key;
    String image_key;
    String title;
    Long update_timestamp;

    VideoCategory[] categories;

    /*待定，需要视频的格式信息*/
    String type;
    Long interval;
    Long size;

    public Video() {
        this.id = 3;
        //this.category_id = 321;
        this.author_id = 123;
        this.video_key = "video_key";
        this.image_key = "image_key";
        this.title = "video";
        this.update_timestamp = 1416121149L;
    }

    public VideoCategory[] getCategories() {
        return categories;
    }

    public void setCategories(VideoCategory[] categories) {
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }*/

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
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
