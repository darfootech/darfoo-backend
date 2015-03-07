package com.springapp.mvc;

import com.darfoo.backend.utils.UploadDirConfig;
import org.junit.Test;

/**
 * Created by zjh on 15-3-7.
 */
public class ConfTests {
    @Test
    public void getUploadDir() {
        System.out.println(UploadDirConfig.uploaddir);
    }
}
