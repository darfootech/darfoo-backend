package com.darfoo.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name="image")
public class Image implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
	@Column(name="IMAGE_KEY",unique=true,nullable=false,columnDefinition="varchar(255) not null")
    String image_key;
	
    public Image() {
        
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
