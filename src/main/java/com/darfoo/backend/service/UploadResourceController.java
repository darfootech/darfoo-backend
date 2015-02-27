package com.darfoo.backend.service;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import com.darfoo.backend.utils.CryptUtils;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by zjh on 14-12-22.
 * 客户端上传视频
 */

@Controller
@RequestMapping("/uploadresource")
public class UploadResourceController {
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    CommonDao commonDao;

    /**
     * 获取上传qiniu云的token凭证
     *
     * @return
     */
    @RequestMapping(value = "gettk", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, String> getUploadToken() {
        HashMap<String, String> result = new HashMap<String, String>();
        String token = qiniuUtils.getToken();
        System.out.println("origin token -> " + token);
        String encryptToken = CryptUtils.base64EncodeStr(token);
        result.put("tk", encryptToken);
        return result;
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
    HashMap<String, String> uploadFinishCallbackWithoutAuth(HttpServletRequest request) {
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            request.setCharacterEncoding("UTF-8");
            String videokey = request.getParameter("videokey");
            String macaddr = videokey.split("\\.")[0].split("-")[2];
            String videotitle = videokey.split("\\.")[0].split("-")[0];

            System.out.println("videokey -> " + videokey);
            System.out.println("macaddr -> " + macaddr);
            System.out.println("videotitle -> " + videotitle);

            HashMap<String, String> insertcontents = new HashMap<String, String>();

            insertcontents.put("title", videotitle);
            insertcontents.put("video_key", videokey);
            insertcontents.put("macaddr", macaddr);

            HashMap<String, Integer> insertresult = commonDao.insertResource(UploadNoAuthVideo.class, insertcontents);

            int status = insertresult.get("statuscode");

            System.out.println("statuscode -> " + status);

            if (status == 200) {
                result.put("status", "ok");
            } else {
                result.put("status", "error");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result.put("status", "error");
        }
        return result;
    }
}
