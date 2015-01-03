package com.springapp.mvc;

import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.AuthorCacheDao;
import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Autowired
    CommonRedisClient redisClient;

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

    @Test
    public void cacheIndexAuthors(){
        List<Author> authors = authorDao.getAllAuthor();
        for (Author author : authors){
            int id = author.getId();
            long result = redisClient.sadd("authorindex", "author-" + id);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        Set<String> indexAuthorKeys = redisClient.smembers("authorindex");
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys){
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }

        System.out.println(result.size());
    }
}
