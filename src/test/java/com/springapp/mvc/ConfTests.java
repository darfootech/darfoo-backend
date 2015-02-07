package com.springapp.mvc;

import org.junit.Test;

import java.io.*;
import java.util.Properties;

/**
 * Created by zjh on 15-2-7.
 */
public class ConfTests {
    @Test
    public void getAdminOpenStatus() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        String propname = "admin.properties";
        try {
            inputStream = new FileInputStream(propname);
            properties.load(inputStream);
            System.out.println(properties.getProperty("open"));
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setAdminOpenStatus() {
        Properties properties = new Properties();
        String propname = "admin.properties";
        try {
            FileOutputStream outputStream = new FileOutputStream(propname);
            properties.setProperty("open", "true");
            properties.store(outputStream, "config admin properties");
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
