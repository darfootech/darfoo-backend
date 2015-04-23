package com.darfoo.backend;

import com.darfoo.backend.utils.DiskFileDirConfig;
import org.junit.Test;

/**
 * Created by zjh on 15-3-7.
 */
public class ConfTests {
    @Test
    public void getUploadDir() {
        System.out.println(DiskFileDirConfig.uploaddir);
    }
}
