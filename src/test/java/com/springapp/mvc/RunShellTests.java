package com.springapp.mvc;

/**
 * Created by zjh on 15-1-11.
 */

import com.darfoo.backend.utils.RunShellUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class RunShellTests {
    @Test
    public void runshellscript() {
        //String scriptpath = "/Users/zjh/Documents/darfoo/darfoo_backend/upload.sh";
        String scriptpath = "./upload.sh";
        System.out.println(RunShellUtils.runshellscript(scriptpath));
    }

    @Test
    public void getresourcevolumn() {
        String scriptpath = "./resourcevolumn.sh";
        System.out.println(RunShellUtils.runshellscriptwithresult(scriptpath));
    }
}
