package com.darfoo.backend.service;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.upload.UploadNoAuthVideoDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.service.responsemodel.UploadStatus;
import com.darfoo.backend.service.responsemodel.UploadToken;
import com.darfoo.backend.utils.CryptUtils;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 14-12-22.
 * 客户端上传视频
 */

@Controller
@RequestMapping("/uploadresource")
public class UploadResourceController {
    @Autowired
    UploadNoAuthVideoDao uploadNoAuthVideoDao;
    @Autowired
    QiniuUtils qiniuUtils;

    /**
     * 获取上传qiniu云的token凭证
     *
     * @return
     */
    @RequestMapping(value = "gettk", method = RequestMethod.GET)
    public
    @ResponseBody
    UploadToken getUploadToken() {
        String token = qiniuUtils.getToken();
        System.out.println("origin token -> " + token);
        String encryptToken = CryptUtils.base64EncodeStr(token);
        return new UploadToken(encryptToken);
    }

    /**
     * 第一版不使用用户验证 自动批量上传
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "finishcallbackna", method = RequestMethod.POST)
    public
    @ResponseBody
    UploadStatus uploadFinishCallbackWithoutAuth(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
            String videokey = request.getParameter("videokey");
            String imagekey = request.getParameter("imagekey");
            String macaddr = videokey.split("\\.")[0].split("-")[2];
            String videotitle = videokey.split("\\.")[0].split("-")[0];
            String videotype = videokey.split("\\.")[1];

            System.out.println("videokey -> " + videokey);
            System.out.println("macaddr -> " + macaddr);
            System.out.println("videotitle -> " + videotitle);

            int status = uploadNoAuthVideoDao.insertUploadVideo(new UploadNoAuthVideo(videokey, imagekey, macaddr, videotitle, videotype, -1));

            if (status == CRUDEvent.INSERT_SUCCESS) {
                return new UploadStatus("ok");
            } else {
                return new UploadStatus("error");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new UploadStatus("error");
        }
    }
}
