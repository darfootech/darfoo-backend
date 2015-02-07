package com.darfoo.backend.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by zjh on 15-2-7.
 */
public class DashboardUtils {
    static String propname = "admin.properties";

    public static boolean isDashboardOpened() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propname);
            properties.load(inputStream);
            String status = properties.getProperty("open");
            inputStream.close();

            if (status.equals("true")) {
                return true;
            }else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setOpenStatus(String status) {
        Properties properties = new Properties();
        try {
            FileOutputStream outputStream = new FileOutputStream(propname);
            properties.setProperty("open", status);
            properties.store(outputStream, "config admin properties");
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDashboard() {
        setOpenStatus("true");
    }

    public static void closeDashboard() {
        setOpenStatus("false");
    }
}
