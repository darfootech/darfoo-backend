package com.darfoo.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dancegroup")
public class DanceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(targetEntity = DanceGroupImage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id")
    DanceGroupImage image;

    @Column(name = "NAME", nullable = false, unique = true, columnDefinition = "varchar(255) not null")
    String name;

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "varchar(255) not null")
    String description;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false, columnDefinition = "bigint(64) not null")
    Long update_timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DanceGroupImage getImage() {
        return image;
    }

    public void setImage(DanceGroupImage image) {
        this.image = image;
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

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String toString() {
        return "name:" + name + "\ndescription:" + description + "\nupdatetime:" + update_timestamp +
                "\nimage_key:" + image.image_key;
    }

}
