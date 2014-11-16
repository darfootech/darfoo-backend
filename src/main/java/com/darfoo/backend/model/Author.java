package com.darfoo.backend.model;

/**
 * Created by zjh on 14-11-16.
 */
public class Author {
    Integer id;
    String name;
    String description;

    public Author() {
        this.id = 3;
        this.name = "ccc";
        this.description = "this is a composer";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
