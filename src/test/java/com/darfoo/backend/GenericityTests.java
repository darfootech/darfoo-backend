package com.darfoo.backend;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-4-21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/pre-deal.xml",
        "file:src/main/webapp/WEB-INF/redis-context.xml",
        "file:src/main/webapp/WEB-INF/springmvc-hibernate.xml",
        "file:src/main/webapp/WEB-INF/util-context.xml"
})
public class GenericityTests {
    @Autowired
    CommonDao commonDao;

    public <T> T getObject(Class<T> c, Integer id) {
        T t = (T) commonDao.getResourceById(c, id);
        return t;
    }

    @Test
    public void getObjectTest() {
        System.out.println(((Version)getObject(Version.class, 40)).getVersion());
    }

}
