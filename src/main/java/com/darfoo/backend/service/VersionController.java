package com.darfoo.backend.service;

import com.darfoo.backend.dao.VersionDao;
import com.darfoo.backend.model.Version;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 15-1-6.
 */

@Controller
public class VersionController {
    @Autowired
    VersionDao versionDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping(value = "/resources/version/latest", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getLatestReleaseVersion(){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Version version = versionDao.getLatestReleaseVersion();
            String version_download_url = qiniuUtils.getQiniuResourceUrl("launcher-" + version.getVersion() + "-release.apk");
            result.put("version", version.getVersion());
            result.put("version_url", version_download_url);
            return result;
        }catch (NullPointerException e){
            result.put("version", "error");
            result.put("version_url", "error");
            return result;
        }
    }

    @RequestMapping(value = "/resources/version/debug/latest", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getLatestDebugVersion(){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Version version = versionDao.getLatestDebugVersion();
            String version_download_url = qiniuUtils.getQiniuResourceUrl("launcher-" + version.getVersion() + "-debug.apk");
            result.put("version", version.getVersion());
            result.put("version_url", version_download_url);
            return result;
        }catch (NullPointerException e){
            result.put("version", "error");
            result.put("version_url", "error");
            return result;
        }
    }

    @RequestMapping(value = "/admin/version/release/new", method = RequestMethod.GET)
    public String uploadReleaseVersion(ModelMap modelMap){
        try {
            Version latestVersion = versionDao.getLatestReleaseVersion();
            modelMap.addAttribute("latestversion", latestVersion.getVersion());
            modelMap.addAttribute("type", "release");
            return "newversion";
        }catch (NullPointerException e){
            modelMap.addAttribute("latestversion", "还没有launcher上传过");
            modelMap.addAttribute("type", "release");
            return "newversion";
        }
    }

    @RequestMapping(value = "/admin/version/release/create", method = RequestMethod.POST)
    public String createReleaseVersion(@RequestParam("versionresource") CommonsMultipartFile versionresource, HttpServletRequest request){
        String newversion = request.getParameter("newversion");
        System.out.println("newversion -> " + newversion);
        Version version = new Version();
        version.setVersion(newversion);
        version.setType("release");
        versionDao.insertVersion(version);

        String versionkey = "launcher-" + newversion + "-release.apk";

        String status = "";

        try {
            status = ServiceUtils.uploadSmallResource(versionresource, versionkey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (status.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }

    @RequestMapping(value = "/admin/version/debug/new", method = RequestMethod.GET)
    public String uploadDebugVersion(ModelMap modelMap){
        try {
            Version latestVersion = versionDao.getLatestDebugVersion();
            modelMap.addAttribute("latestversion", latestVersion.getVersion());
            modelMap.addAttribute("type", "debug");
            return "newversion";
        }catch (NullPointerException e){
            modelMap.addAttribute("latestversion", "还没有launcher上传过");
            modelMap.addAttribute("type", "debug");
            return "newversion";
        }
    }

    @RequestMapping(value = "/admin/version/debug/create", method = RequestMethod.POST)
    public String createDebugVersion(@RequestParam("versionresource") CommonsMultipartFile versionresource, HttpServletRequest request){
        String newversion = request.getParameter("newversion");
        System.out.println("newversion -> " + newversion);
        Version version = new Version();
        version.setVersion(newversion);
        version.setType("debug");
        versionDao.insertVersion(version);

        String versionkey = "launcher-" + newversion + "-debug.apk";

        String status = "";

        try {
            status = ServiceUtils.uploadSmallResource(versionresource, versionkey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (status.equals("200")){
            return "success";
        }else{
            return "fail";
        }
    }
}
