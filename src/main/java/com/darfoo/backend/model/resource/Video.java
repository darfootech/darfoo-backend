package com.darfoo.backend.model.resource;

import com.darfoo.backend.caches.cota.CacheInsert;
import com.darfoo.backend.caches.cota.CacheInsertEnum;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.cota.ModelInsert;
import com.darfoo.backend.model.cota.ModelUpdate;
import com.darfoo.backend.model.cota.ModelUpload;
import com.darfoo.backend.model.cota.ModelUploadEnum;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjh on 14-11-16.
 * modify by yy on 14-11-22
 */
@Entity
@Table(name = "video")
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Integer id;

    @OneToOne(targetEntity = Image.class, fetch = FetchType.EAGER)
    @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id", updatable = true)
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    Image image;

    @ManyToOne(targetEntity = Author.class)
    @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id", updatable = true)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Author author;

    @ManyToMany(targetEntity = VideoCategory.class, fetch = FetchType.EAGER)
    @JoinTable(name = "video_category", joinColumns = {@JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false, columnDefinition = "int(11) not null")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", nullable = false, columnDefinition = "int(11) not null")})
    Set<VideoCategory> categories = new HashSet<VideoCategory>();

    @Column(name = "VIDEO_KEY", unique = true, nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    String video_key;

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    String title;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false, columnDefinition = "bigint(64) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Long update_timestamp;

    @ManyToOne(targetEntity = Music.class)
    @JoinColumn(name = "MUSIC_ID", referencedColumnName = "id", updatable = true, nullable = true)
    Music music;

    //点击量
    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    Long hottest;

    @Column(name = "RECOMMEND", nullable = true, updatable = true, columnDefinition = "int default 0")
    Integer recommend;

    @Transient
    @ModelInsert
    String videotype;

    @Transient
    @ModelInsert
    @ModelUpdate
    String authorname;

    @Transient
    @ModelInsert
    @ModelUpload(type = ModelUploadEnum.SMALL)
    String imagekey;

    @Transient
    @ModelInsert
    @ModelUpdate
    String connectmusic;

    @Transient
    @ModelUpload(type = ModelUploadEnum.LARGE)
    String videokey;

    public Video() {
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }


    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Integer getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<VideoCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<VideoCategory> categories) {
        this.categories = categories;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideo_key() {
        return video_key;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(Long update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String toString(boolean isShowCategory) {
        StringBuilder sb = new StringBuilder();
        sb.append("title:" + title + "\nvideo_key:" + video_key + "\nupdate_timestamp:" + update_timestamp);
        if (author == null) {
            sb.append("\nauthor:null");
        } else {
            sb.append("\nauthor:" + author.getName() + "  演唱家信息:" + author.getDescription());
            if (author.getImage() == null) {
                sb.append(" 作者图片:" + null);
            } else {
                sb.append(" 作者图片:" + author.getImage().getImage_key());
            }
        }
        if (image == null) {
            sb.append("\n视频图片:null");
        } else {
            sb.append("\n视频图片:" + image.getImage_key());
        }
        if (isShowCategory) {
            if (categories == null) {
                sb.append("种类为空");
            } else {
                for (VideoCategory category : categories) {
                    sb.append("\n").append("种类:" + category.getTitle() + " 描述:" + category.getDescription());
                }
            }
        }
        return sb.toString();
    }
}
