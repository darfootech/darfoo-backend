package com.darfoo.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name = "educationcategory")
public class TutorialCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "TITLE", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    String title;
    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "varchar(255) not null")
    String description;
    //建立与video表的多对多关系
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories")
//	@JoinTable(name="education_category",joinColumns={@JoinColumn(name="category_id",referencedColumnName="id",nullable=false,columnDefinition="int(11) not null")},
//	inverseJoinColumns={@JoinColumn(name="video_id",nullable=false,columnDefinition="int(11) not null")})
            Set<Tutorial> videos = new HashSet<Tutorial>();

    public TutorialCategory() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tutorial> getVideos() {
        return videos;
    }

    public void setVideos(Set<Tutorial> videos) {
        this.videos = videos;
    }


}
