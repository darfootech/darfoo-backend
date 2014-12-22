package com.darfoo.backend.service;

import com.darfoo.backend.service.responsemodel.UploadToken;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zjh on 14-12-22.
 * 客户端上传视频
 */

@Controller
@RequestMapping("/uploadresource")
public class UploadResourceController {

    QiniuUtils qiniuUtils = new QiniuUtils();

    /**
     * 获取上传qiniu云的token凭证
     * @return
     */
    @RequestMapping(value = "gettk", method = RequestMethod.GET)
    public @ResponseBody
    UploadToken getUploadToken(){
        String token = qiniuUtils.getToken();
        return new UploadToken(token);
    }
}
