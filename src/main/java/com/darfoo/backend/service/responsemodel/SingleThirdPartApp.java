package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 15-4-28.
 */
public class SingleThirdPartApp {
    Integer id;
    String title;
    String app_url;

    public SingleThirdPartApp() {
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

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    @Override
    public String toString() {
        return "SingleThirdPartApp{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", app_url='" + app_url + '\'' +
                '}';
    }
}
