package com.darfoo.backend.model;

import com.darfoo.backend.caches.cota.CacheInsert;
import com.darfoo.backend.caches.cota.CacheInsertEnum;
import com.darfoo.backend.model.cota.annotations.ModelInsert;
import com.darfoo.backend.model.cota.annotations.ModelOperation;
import com.darfoo.backend.model.cota.annotations.ModelUpdate;
import com.darfoo.backend.model.cota.annotations.ModelUpload;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.model.resource.Image;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-4-26.
 */

@Entity
@Table(name = "thirdpartapp")
@ModelOperation(insertMethod = "insertThirdPartApp", updateMethod = "updateTitle")
public class ThirdPartApp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Integer id;

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    String title;

    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    String app_key;

    public ThirdPartApp() {
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

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }
}
