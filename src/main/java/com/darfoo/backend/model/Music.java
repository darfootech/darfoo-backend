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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name="music")
public class Music implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
	
	//music & musiccategory 双向N-N
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="music_category",joinColumns={@JoinColumn(name="music_id",referencedColumnName="id",nullable=false,columnDefinition="int(11) not null")},
	inverseJoinColumns={@JoinColumn(name="category_id",nullable=false,columnDefinition="int(11) not null")})
	Set<MusicCategory> categories = new HashSet<MusicCategory>();
	
	//music & author 单向N-1 在music表中增加一个外键列AUTHOR_ID(author的主键)
	@ManyToOne(targetEntity=Author.class)
	@JoinColumn(name="AUTHOR_ID",referencedColumnName="id")
	Author author;
	
	//music & image 单向N-1 在music表中增加一个外键列IMAGE_ID(image的主键)
	@ManyToOne(targetEntity=Image.class)
	@JoinColumn(name="IMAGE_ID",referencedColumnName="id")
	Image image;
	
	@Column(name="TITLE",nullable=false,columnDefinition="varchar(255) not null")
    String title;
	
	@Column(name="MUSIC_KEY",nullable=false,columnDefinition="varchar(255) not null")
    String music_key;
	
	@Column(name="UPDATE_TIMESTAMP",nullable=false,columnDefinition="bigint(64) not null")
    Long update_timestamp;


    public Music() {
    }






	public Author getAuthor() {
		return author;
	}






	public void setAuthor(Author author) {
		this.author = author;
	}






	public Set<MusicCategory> getCategories() {
		return categories;
	}



	public void setCategories(Set<MusicCategory> categories) {
		this.categories = categories;
	}



	public Image getImage() {
		return image;
	}



	public void setImage(Image image) {
		this.image = image;
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

    public String getMusic_key() {
        return music_key;
    }

    public void setMusic_key(String music_key) {
        this.music_key = music_key;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }
}
