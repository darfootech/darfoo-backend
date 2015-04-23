package com.darfoo.backend.model.resource.opera;

import com.darfoo.backend.caches.cota.CacheInsert;
import com.darfoo.backend.caches.cota.CacheInsertEnum;
import com.darfoo.backend.model.cota.annotations.*;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.model.cota.enums.OperaVideoType;
import com.darfoo.backend.model.resource.Image;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-4-11.
 */

//单个越剧视频
@Entity
@Table(name = "operavideo")
@ModelOperation(insertMethod = "insertOperaVideo", updateMethod = "updateOperaVideo")
public class OperaVideo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Integer id;

    //视频关联的封面图片
    @OneToOne(targetEntity = Image.class, fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id", updatable = true)
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    Image image;

    //视频关联的舞队 一个舞队可以有多个视频
    @ManyToOne(targetEntity = OperaSeries.class)
    @Cascade(value = {org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "SERIES_ID", referencedColumnName = "id", nullable = true)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @CSVTitle(title = "越剧连续剧名字")
    OperaSeries series;

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    @CSVTitle(title = "视频标题")
    String title;

    @Column(name = "VIDEO_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    String video_key;

    //点击量
    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    Long hottest = 0L;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false, columnDefinition = "bigint(64) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Long update_timestamp = System.currentTimeMillis();

    //欣赏 教学
    @Column(name = "VIDEO_TYPE", nullable = true, updatable = true, columnDefinition = "int default 0")
    @CSVTitle(title = "视频类型")
    @ModelUpdate
    OperaVideoType type;

    //当前越剧视频在与之关联的越剧连续剧中的顺序位置
    @Column(name = "VIDEO_ORDER", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    @ModelInsert
    @ModelUpdate
    Integer order = 0;

    //mp4 flv
    @Transient
    @ModelInsert
    String videotype;

    @Transient
    @ModelInsert
    @ModelUpdate
    String seriesname;

    @Transient
    @ModelUpload(type = ModelUploadEnum.SMALL)
    String imagekey;

    @Transient
    @ModelUpload(type = ModelUploadEnum.LARGE)
    String videokey;

    public OperaVideo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public OperaSeries getSeries() {
        return series;
    }

    public void setSeries(OperaSeries series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }

    public OperaVideoType getType() {
        return type;
    }

    public void setType(OperaVideoType type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OperaVideo{" +
                "id=" + id +
                ", image=" + image +
                ", series=" + series +
                ", title='" + title + '\'' +
                ", video_key='" + video_key + '\'' +
                ", hottest=" + hottest +
                ", type=" + type +
                '}';
    }
}
