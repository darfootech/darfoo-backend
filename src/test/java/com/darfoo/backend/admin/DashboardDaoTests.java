package com.darfoo.backend.admin;

/**
 * Created by zjh on 15-2-8.
 */

import com.darfoo.backend.dao.DashboardDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DashboardDaoTests {
    @Autowired
    DashboardDao dashboardDao;

    @Test
    public void noTriggerThere() {
        System.out.println(dashboardDao.isNoTriggerThere());
    }

    @Test
    public void isDashboardOpen() {
        System.out.println(dashboardDao.isDashboardOpen());
    }

    @Test
    public void isDashboardClose() {
        System.out.println(dashboardDao.isDashboardClose());
    }

    @Test
    public void openDashboard() {
        dashboardDao.openDashBoard();
    }

    @Test
    public void closeDashboard() {
        dashboardDao.closeDashBoard();
    }
}
