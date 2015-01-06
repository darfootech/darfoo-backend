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
    Map<String, Object> getLatestVersion(){
        Map<String, Object> result = new HashMap<String, Object>();
        Version version = versionDao.getLatestVersion();
        String version_download_url = qiniuUtils.getQiniuResourceUrl("launcher-" + version.getVersion() + "version.apk");
        result.put("version", version.getVersion());
        result.put("version_url", version_download_url);
        return result;
    }

    @RequestMapping(value = "/admin/version/new", method = RequestMethod.GET)
    public String uploadVersion(ModelMap modelMap){
        Version latestVersion = versionDao.getLatestVersion();
        modelMap.addAttribute("latestversion", latestVersion.getVersion());
        return "newversion";
    }

    @RequestMapping(value = "/admin/version/create", method = RequestMethod.POST)
    public String createVersion(@RequestParam("versionresource") CommonsMultipartFile versionresource, HttpServletRequest request){
        String newversion = request.getParameter("newversion");
        System.out.println("newversion -> " + newversion);
        Version version = new Version();
        version.setVersion(newversion);
        versionDao.insertVersion(version);

        String versionkey = "launcher-" + newversion + "version.apk";

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
