package com.darfoo.backend.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-1-6.
 */
@Entity
@Table(name = "version")
public class Version implements Serializable {
    //引用下面名为mysql的主键生成方式
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "version", nullable = false, columnDefinition = "varchar(255) not null")
    String version;

    //分为debug和release两种模式 debug是一版launcher上线之后先测试更新的接口 不影响正常用户 等测试成功上传release的launcher供正常用户下载更新
    @Column(name = "type", nullable = false, columnDefinition = "varchar(255) not null")
    String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
