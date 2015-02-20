package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Created by zjh on 15-2-20.
 */

//数据库初始化的时候需要运行这些东西来插入资源的类别
public class CategoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void insertResourceCategories(Class resource, String[] categories) {
        try {
            Session session = sessionFactory.getCurrentSession();
            for (String category : categories) {
                Object object = resource.newInstance();
                Field titleField = resource.getField("title");
                Field descField = resource.getField("description");
                titleField.setAccessible(true);
                descField.setAccessible(true);

                titleField.set(object, category);
                descField.set(object, "待定");

                session.save(object);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //插入所有video(视频)的类型
    public void insertAllVideoCategories() {
        String[] categories = {
                "较快", "适中", "较慢", //按速度
                "简单", "普通", "稍难", //按难度
                "欢快", "活泼", "优美", "情歌风", "红歌风", "草原风", "戏曲风", "印巴风", "江南风", "民歌风", "儿歌风", //按风格
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        };

        insertResourceCategories(VideoCategory.class, categories);
    }

    //插入所有education(视频)的类型  (暂时将名师教学这个选项去掉)
    public void insertAllTutorialCategories() {
        String[] categories = {"快", "中", "慢",    //按速度
                "简单", "适中", "稍难",                    //按难度
                "队形表演", "背面教学", "分解教学"};  //按教学类型

        insertResourceCategories(TutorialCategory.class, categories);
    }

    //插入所有music(categories)的类型
    public void insertAllMusicCategories() {
        String[] categories = {"四拍", "八拍", "十六拍", "三十二拍",    //按节拍
                "情歌风", "红歌风", "草原风", "戏曲风", "印巴风", "江南风", "民歌风", "儿歌风",  //按风格
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};//按字母

        insertResourceCategories(MusicCategory.class, categories);
    }
}
