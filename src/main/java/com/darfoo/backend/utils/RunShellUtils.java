package com.darfoo.backend.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zjh on 15-1-11.
 */
public class RunShellUtils {
    public static int runshellscript(String scriptPath){
        try {
            Process ps = Runtime.getRuntime().exec(scriptPath);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
            return 1;
        }
        catch (Exception e) {
            //e.printStackTrace();
            return 0;
        }
    }
}
