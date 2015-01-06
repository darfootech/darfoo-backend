package com.darfoo.backend.service;

import com.darfoo.backend.dao.VersionDao;
import com.darfoo.backend.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 15-1-6.
 */

@Controller
@RequestMapping("/resources/version")
public class VersionController {
    @Autowired
    VersionDao versionDao;

    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getLatestVersion(){
        Map<String, Object> result = new HashMap<String, Object>();
        Version version = versionDao.getLatestVersion();
        result.put("version", version.getVersion());
        return result;
    }
}
