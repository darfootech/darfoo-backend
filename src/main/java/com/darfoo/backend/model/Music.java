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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Created by zjh on 14-11-16.
 */
@Entity
@Table(name = "music")
public class Music implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    //music & musiccategory 双向N-N
    @ManyToMany(targetEntity = MusicCategory.class)
    @JoinTable(name = "music_category", joinColumns = {@JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, columnDefinition = "int(11) not null")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", nullable = false, columnDefinition = "int(11) not null")})
    Set<MusicCategory> categories = new HashSet<MusicCategory>();

    //music & author 单向N-1 在music表中增加一个外键列AUTHOR_ID(author的主键)
    @ManyToOne(targetEntity = Author.class)
    @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE}) //去掉级联删除
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id")
    Author author;

    //music & image 单向N-1 在music表中增加一个外键列IMAGE_ID(image的主键)
    @ManyToOne(targetEntity = Image.class)
    @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})  //去掉级联删除
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id")
    Image image;

    @Column(name = "TITLE", nullable = false, columnDefinition = "varchar(255) not null")
    String title;

    @Column(name = "MUSIC_KEY", nullable = false, unique = true, columnDefinition = "varchar(255) not null")
    String music_key;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false, columnDefinition = "bigint(64) not null")
    Long update_timestamp;

    //点击量
    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 0")
    Long hottest;

    @Column(name = "AUTHOR_NAME", nullable = true, columnDefinition = "varchar(255) not null")
    String authorName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }

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

    public String toString(boolean isShowCategory) {
        StringBuilder sb = new StringBuilder();
        sb.append("title:" + title + "\nmusic_key:" + music_key + "\nupdate_timestamp:" + update_timestamp);
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
                for (MusicCategory category : categories) {
                    sb.append("\n").append("种类:" + category.title + " 描述:" + category.description);
                }
            }
        }
        return sb.toString();
    }

    public void trigLazyLoad() {
        for (MusicCategory category : categories)
            ;
    }
}
