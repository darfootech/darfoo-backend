package com.darfoo.backend.service;

import com.darfoo.backend.dao.VersionDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Version;
import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 15-1-6.
 */

@Controller
public class VersionController {
    @Autowired
    VersionDao versionDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    QiniuUtils qiniuUtils;

    @RequestMapping(value = "/resources/version/{type}/latest", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getLatestVersion(@PathVariable String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Version version = versionDao.getLatestVersion(Version.class, type);
            String version_download_url = qiniuUtils.getQiniuResourceUrl(String.format("launcher-%s-%s.apk", version.getVersion(), type), QiniuResourceEnum.RAW);
            result.put("version", version.getVersion());
            result.put("version_url", version_download_url);
            return result;
        } catch (NullPointerException e) {
            result.put("version", "error");
            result.put("version_url", "error");
            return result;
        }
    }

    @RequestMapping(value = "/admin/version/{type}/new", method = RequestMethod.GET)
    public String uploadVersion(@PathVariable String type, ModelMap modelMap) {
        try {
            Version latestVersion = versionDao.getLatestVersion(Version.class, type);
            modelMap.addAttribute("latestversion", latestVersion.getVersion());
            modelMap.addAttribute("type", type);
            return "version/newversion";
        } catch (NullPointerException e) {
            modelMap.addAttribute("latestversion", "还没有launcher上传过");
            modelMap.addAttribute("type", type);
            return "version/newversion";
        }
    }

    @RequestMapping(value = "/admin/version/{type}/create", method = RequestMethod.POST)
    public String createVersion(@PathVariable String type, @RequestParam("versionresource") CommonsMultipartFile versionresource, HttpServletRequest request) {
        String newversion = request.getParameter("newversion");
        System.out.println("newversion -> " + newversion);
        Version version = new Version();
        version.setVersion(newversion);
        version.setType(type);
        versionDao.insertVersion(version);

        String versionkey = String.format("launcher-%s-%s.apk", newversion, type);

        String status = ServiceUtils.uploadSmallResource(versionresource, versionkey);

        if (status.equals("200")) {
            return "success";
        } else {
            return "fail";
        }
    }
}