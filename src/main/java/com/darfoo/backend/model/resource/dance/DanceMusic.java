package com.darfoo.backend.model.resource.dance;

import com.darfoo.backend.caches.cota.CacheInsert;
import com.darfoo.backend.caches.cota.CacheInsertEnum;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.cota.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name = "dancemusic")
@ModelOperation(insertMethod = "insertDanceMusic", updateMethod = "updateDanceMusic")
public class DanceMusic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Integer id;

    //music & musiccategory 双向N-N
    @ManyToMany(targetEntity = DanceMusicCategory.class, fetch = FetchType.EAGER)
    @JoinTable(name = "dancemusic_category", joinColumns = {@JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, columnDefinition = "int(11) not null")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", nullable = false, columnDefinition = "int(11) not null")})
    Set<DanceMusicCategory> categories = new HashSet<DanceMusicCategory>();

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    String title;

    @Column(name = "MUSIC_KEY", nullable = false, unique = true, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.RESOURCE)
    String music_key;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false, columnDefinition = "bigint(64) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    Long update_timestamp = System.currentTimeMillis();

    //点击量
    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    Long hottest = 0L;

    //因为伴奏的舞队不需要其他的信息 所以伴奏只需要一个舞队名字就可以了
    @Column(name = "AUTHOR_NAME", nullable = true, columnDefinition = "varchar(255) not null")
    @CacheInsert(type = CacheInsertEnum.NORMAL)
    @ModelInsert
    @ModelUpdate
    String authorname;

    @Transient
    @ModelUpload(type = ModelUploadEnum.LARGE)
    String musickey;

    public DanceMusic() {
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }

    public Set<DanceMusicCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<DanceMusicCategory> categories) {
        this.categories = categories;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("title:" + title + "\nmusic_key:" + music_key + "\nupdate_timestamp:" + update_timestamp);
        return sb.toString();
    }
}
