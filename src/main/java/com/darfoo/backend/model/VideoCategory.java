package com.darfoo.backend.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name="videocategroy")
public class VideoCategory implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
	@Column(name="TITLE",unique=true,nullable=false,columnDefinition="varchar(255) not null")
    String title;
	@Column(name="DESCRIPTION",nullable=false,columnDefinition="varchar(255) not null")
    String description;
	//建立与video表的多对多关系
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="video_category",joinColumns={@JoinColumn(name="category_id",referencedColumnName="id",nullable=false,columnDefinition="int(11) not null")},
	inverseJoinColumns={@JoinColumn(name="video_id",nullable=false,columnDefinition="int(11) not null")})
    Set<Video>  videos = new HashSet<Video>();

    public VideoCategory() {
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

	public Set<Video> getVideos() {
		return videos;
	}

	public void setVideos(Set<Video> videos) {
		this.videos = videos;
	}


}
