package com.darfoo.backend.service;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.UploadNoAuthVideoDao;
import com.darfoo.backend.dao.UploadVideoDao;
import com.darfoo.backend.model.UploadNoAuthVideo;
import com.darfoo.backend.model.UploadVideo;
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
    UploadVideoDao uploadVideoDao;
    @Autowired
    UploadNoAuthVideoDao uploadNoAuthVideoDao;

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
        String encryptToken = CryptUtils.base64EncodeStr(token);
        return new UploadToken(encryptToken);
    }

    /**
     * launcher在上传视频之前要先确定相同的videokey是否已经上传过了
     * @param videokey
     * @return
     */
    @RequestMapping(value = "prepareupload/{videokey}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> prepareupload(@PathVariable String videokey){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean status = uploadVideoDao.isExistVideo(videokey);

        if (!status){
            result.put("status", "ok");
        }else{
            result.put("status", "error");
        }

        return result;
    }

    /**
     * 当客户端成功将资源上传至七牛之后需要请求这个回调请求，将资源有关信息发送给服务器数据库
     * @param request
     * @return
     */
    @RequestMapping(value = "finishcallback", method = RequestMethod.POST)
    public @ResponseBody
    UploadStatus uploadFinishCallback(HttpServletRequest request){
        int userid = Integer.parseInt(request.getParameter("userid"));
        String videokey = request.getParameter("videokey");
        System.out.println("post userid -> " + userid);
        System.out.println("post authorid -> " + videokey);

        int status = uploadVideoDao.insertUploadVideo(new UploadVideo(videokey, userid, -1));

        if (status == CRUDEvent.INSERT_SUCCESS){
            return new UploadStatus("ok");
        }else{
            return new UploadStatus("error");
        }
    }

    /**
     * 第一版不使用用户验证 自动批量上传
     * @param request
     * @return
     */
    @RequestMapping(value = "finishcallbackna", method = RequestMethod.POST)
    public @ResponseBody UploadStatus uploadFinishCallbackWithoutAuth(HttpServletRequest request){
        String videokey = request.getParameter("videokey");
        String imagekey = request.getParameter("imagekey");
        String macaddr = videokey.split("\\.")[0].split("-")[2];
        String videotitle = videokey.split("\\.")[0].split("-")[0];

        System.out.println("videokey -> " + videokey);
        System.out.println("macaddr -> " + macaddr);
        System.out.println("videotitle -> " + videotitle);

        int status = uploadNoAuthVideoDao.insertUploadVideo(new UploadNoAuthVideo(videokey, imagekey, macaddr, videotitle, -1));

        if (status == CRUDEvent.INSERT_SUCCESS){
            return new UploadStatus("ok");
        }else{
            return new UploadStatus("error");
        }
    }
}
