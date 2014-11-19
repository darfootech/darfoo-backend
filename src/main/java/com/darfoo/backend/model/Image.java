package com.darfoo.backend.model;

import java.io.Serializable;

/**
 * Created by zjh on 14-11-16.
 */
public class Image implements Serializable {
    Integer id;
    String image_key;

    public Image(String image_key) {
        this.image_key = image_key;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
