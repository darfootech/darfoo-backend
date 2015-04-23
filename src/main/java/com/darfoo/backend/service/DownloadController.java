package com.darfoo.backend.service;

import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.DownloadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Created by zjh on 14-12-11.
 */

//下载统计报表
@Controller
public class DownloadController {
    @Autowired
    DownloadUtils downloadUtils;

    @RequestMapping(value = "/admin/download/{type}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadResources(@PathVariable String type) throws IOException {
        downloadUtils.writeResourcesToCSV(TypeClassMapping.typeClassMap.get(type));
        return downloadUtils.downloadCSVFiles(type);
    }

    @RequestMapping(value = "/admin/download/stat/{type}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadStatResources(@PathVariable String type) throws IOException {
        downloadUtils.writeStatisticDataToCSV(TypeClassMapping.typeClassMap.get(type));
        return downloadUtils.downloadCSVFiles(type);
    }

    @RequestMapping(value = "/admin/download/{type}/videos/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAuthorvideos(@PathVariable String type, @PathVariable Integer id) throws IOException {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        downloadUtils.writeVideosOfResourceToCSV(resource, id);
        return downloadUtils.downloadCSVFiles(String.format("%svideos-%d", resource.getSimpleName().toLowerCase(), id));
    }
}