package com.darfoo.backend.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-1-6.
 */
@Entity
@Table(name = "version")
public class Version implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //引用下面名为mysql的主键生成方式
            Integer id;
    @Column(name = "version", nullable = false, columnDefinition = "varchar(255) not null")
    String version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
