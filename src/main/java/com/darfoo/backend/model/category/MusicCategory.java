package com.darfoo.backend.model.category;

import com.darfoo.backend.model.resource.Music;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name = "musiccategory")
public class MusicCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "Title", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String title;
    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "varchar(255) not null")
    String description;
    //建立于music表的 双向N-N关系
    @ManyToMany(mappedBy = "categories", targetEntity = Music.class)
    Set<Music> musics = new HashSet<Music>();

    public MusicCategory() {
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

    public Set<Music> getMusics() {
        return musics;
    }

    public void setMusics(Set<Music> musics) {
        this.musics = musics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
