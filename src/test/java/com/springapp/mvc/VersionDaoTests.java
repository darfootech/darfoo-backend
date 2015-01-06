package com.springapp.mvc;

/**
 * Created by zjh on 15-1-6.
 */

import com.darfoo.backend.dao.VersionDao;
import com.darfoo.backend.model.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class VersionDaoTests {
    @Autowired
    VersionDao versionDao;

    @Test
    public void insertNewVersion(){
        String version = "1.0.9";
        Version v = new Version();
        v.setVersion(version);
        versionDao.insertVersion(v);
    }


    @Test
    public void getLatestVersion(){
        try {
            Version latestVersion = versionDao.getLatestVersion();
            System.out.println("latest version -> " + latestVersion.getVersion());
        }catch (NullPointerException e){
            System.out.println("no latest version already exists");
        }
    }
}
