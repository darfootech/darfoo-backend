package com.darfoo.backend.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zjh on 15-4-16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DeleteDaoTests {
    @Autowired
    CommonDao commonDao;

    @Test
    public void deleteOperaSeries() {
        Integer id = 2;
        commonDao.deleteResourceById(OperaSeries.class, id);
    }
}
