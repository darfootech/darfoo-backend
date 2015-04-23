package com.darfoo.backend.model.resource.opera;

import com.darfoo.backend.caches.cota.CacheInsert;
import com.darfoo.backend.caches.cota.CacheInsertEnum;
import com.darfoo.backend.model.cota.annotations.*;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.model.resource.Image;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-4-11.
 */

//一个越剧连续剧 类似广场舞中的舞队
@Entity
@Table(name = "operaseries")
@ModelOperation(insertMethod = "insertOperaSeries", updateMethod = "updateOperaSeries")
public class OperaSeries implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Integer id;

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    @CSVTitle(title = "越剧连续剧名字")
    String title;

    @OneToOne(targetEntity = Image.class, fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id", updatable = true)
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    Image image;

    @Transient
    @ModelInsert
    @ModelUpload(type = ModelUploadEnum.SMALL)
    @ModelUpdate
    String imagekey;

    //点击量
    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    Long hottest = 0L;

    public OperaSeries() {
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }

    @Override
    public String toString() {
        return "OperaSeries{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image=" + image +
                ", hottest=" + hottest +
                '}';
    }
}
