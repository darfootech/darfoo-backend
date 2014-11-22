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

import org.hibernate.annotations.GenericGenerator;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name="author")
public class Author implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //引用下面名为mysql的主键生成方式
    Integer id;
	@Column(name="NAME",nullable=false,columnDefinition="varchar(255) not null")
    String name;
	@Column(name="DESCRIPTION",nullable=false,columnDefinition="varchar(255) not null")
    String description;
	
    public Author() {

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
