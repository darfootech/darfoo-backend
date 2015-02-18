package com.springapp.mvc;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            inputStream = getClass().getClassLoader().getResourceAsStream(propname);
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
            String file = getClass().getClassLoader().getResource(propname).getFile();
            System.out.println(file);
            FileOutputStream outputStream = new FileOutputStream(file);
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
