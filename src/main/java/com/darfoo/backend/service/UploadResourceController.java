package com.darfoo.backend.service;

import com.darfoo.backend.service.responsemodel.UploadStatus;
import com.darfoo.backend.service.responsemodel.UploadToken;
import com.darfoo.backend.utils.CryptUtils;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zjh on 14-12-22.
 * 客户端上传视频
 */

@Controller
@RequestMapping("/uploadresource")
public class UploadResourceController {
    CryptUtils cryptUtils = new CryptUtils();
    QiniuUtils qiniuUtils = new QiniuUtils();

    /**
     * 获取上传qiniu云的token凭证
     * @return
     */
    @RequestMapping(value = "gettk", method = RequestMethod.GET)
    public @ResponseBody
    UploadToken getUploadToken(){
        String token = qiniuUtils.getToken();
        System.out.println("origin token -> " + token);
        String encryptToken = cryptUtils.base64EncodeStr(token);
        return new UploadToken(encryptToken);
    }

    /**
     * 当客户端成功将资源上传至七牛之后需要请求这个回调请求，将资源有关信息发送给服务器数据库
     * @param request
     * @return
     */
    @RequestMapping(value = "finishcallback", method = RequestMethod.POST)
    public @ResponseBody
    UploadStatus uploadFinishCallback(HttpServletRequest request){
        System.out.println("post title -> " + request.getParameter("title"));
        System.out.println("post authorid -> " + request.getParameter("authorid"));

        return new UploadStatus("ok");
    }
}
