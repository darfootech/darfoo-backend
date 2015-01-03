package com.springapp.mvc;

import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-1-3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml"
})
public class AuthorCacheTests {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    AuthorCacheDao authorCacheDao;

    @Test
    public void cacheSingleAuthor(){
        Author author = authorDao.getAuthor(1);
        System.out.println(authorCacheDao.insertSingleAuthor(author));
    }

    @Test
    public void getSingleAuthor(){
        Integer id = 1;
        SingleAuthor author = authorCacheDao.getSingleAuthor(id);
        System.out.println(author.getName());
    }
}
