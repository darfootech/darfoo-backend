package com.darfoo.backend.offlinejobs;

import com.darfoo.backend.dao.cota.CategoryDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-2-20.
 */

//->初始化数据库时插入资源类别
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class InsertResourceCategory {
    @Autowired
    CategoryDao categoryDao;

    @Test
    public void insertAllVideoCategories() {
        categoryDao.insertAllVideoCategories();
    }

    @Test
    public void insertAllTutorialCategories() {
        categoryDao.insertAllTutorialCategories();
    }

    @Test
    public void insertAllMusicCategories() {
        categoryDao.insertAllMusicCategories();
    }
}
