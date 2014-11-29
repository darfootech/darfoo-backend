package com.darfoo.backend.service;

import com.darfoo.backend.utils.FileUtils;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by zjh on 14-11-27.
 * 用于上传伴奏，视频和教学视频
 */

@Controller
public class UploadController {
    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping(value = "/resources/video/new", method = RequestMethod.GET)
    public String uploadVideo(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "video");
        modelMap.addAttribute("resource", "video");
        return "uploadvideo";
    }

    @RequestMapping(value = "/resources/video/create", method = RequestMethod.POST)
    public @ResponseBody String createVideo(HttpServletRequest request){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/music/new", method = RequestMethod.GET)
    public String uploadMusic(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "music");
        modelMap.addAttribute("resource", "music");
        return "uploadmusic";
    }

    @RequestMapping(value = "/resources/music/create", method = RequestMethod.POST)
    public @ResponseBody String createMusic(HttpServletRequest request){
        String musicTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String musicBeat = request.getParameter("musicbeat");
        String musicStyle = request.getParameter("musicstyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + musicTitle + " " + authorName + " " + imagekey + " " + musicBeat + " " + musicStyle + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "/resources/tutorial/new", method = RequestMethod.GET)
    public String uploadTutorial(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "tutorial");
        modelMap.addAttribute("resource", "tutorial");
        return "uploadtutorial";
    }

    @RequestMapping(value = "/resources/tutorial/create", method = RequestMethod.POST)
    public @ResponseBody String createTutorial(HttpServletRequest request){
        String videoTitle = request.getParameter("title");
        String authorName = request.getParameter("authorname");
        String imagekey = request.getParameter("imagekey");
        String videoSpeed = request.getParameter("videospeed");
        String videoDifficult = request.getParameter("videodifficult");
        String videoStyle = request.getParameter("videostyle");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + videoTitle + " " + authorName + " " + imagekey + " " + videoSpeed + " " + videoDifficult + " " + videoStyle + " " + update_timestamp);
        return "cleantha";
    }

    //作者信息，不管是视频作者还是伴奏作者
    @RequestMapping(value = "/resources/author/new", method = RequestMethod.GET)
    public String uploadAuthor(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "author");
        modelMap.addAttribute("resource", "author");
        return "uploadauthor";
    }

    @RequestMapping(value = "/resources/author/create", method = RequestMethod.POST)
    public @ResponseBody String createAuthor(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        System.out.println("requests: " + name + " " + description);
        return "cleantha";
    }

    //新建舞队
    @RequestMapping(value = "/resources/team/new", method = RequestMethod.GET)
    public String uploadTeam(ModelMap modelMap, HttpSession session){
        session.setAttribute("resource", "team");
        modelMap.addAttribute("resource", "team");
        return "uploadteam";
    }

    @RequestMapping(value = "/resources/team/create", method = RequestMethod.POST)
    public @ResponseBody String createTeam(HttpServletRequest request){
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String imagekey = request.getParameter("imagekey");
        Long update_timestamp = System.currentTimeMillis() / 1000;
        System.out.println("requests: " + name + " " + description + " " + imagekey + " " + update_timestamp);
        return "cleantha";
    }

    @RequestMapping(value = "startupload", method = RequestMethod.GET)
    public String startUpload(){
        return "uploadfile";
    }

    /*
     * 通过流的方式上传文件
     * @RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
     */
    @RequestMapping("/uploadfile1")
    public String  fileUpload(@RequestParam("file") CommonsMultipartFile file) throws IOException {
        //用来检测程序运行时间
        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());

        try {
            //获取输出流
            OutputStream os=new FileOutputStream("./"+new Date().getTime()+file.getOriginalFilename());
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            InputStream is= file.getInputStream();
            int temp;
            //一个一个字节的读取并写入
            while((temp=is.read())!=(-1))
            {
                os.write(temp);
            }
            os.flush();
            os.close();
            is.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法一的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "/success";
    }

    /*
     * 采用file.Transto 来保存上传的文件
     */
    @RequestMapping("/uploadfile2")
    public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file) throws IOException {
        //删除目录
        String fileDir = "./uploadfiles";
        FileUtils.delete(fileDir);

        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());

        //创建目录
        String dirName = "./uploadfiles";
        FileUtils.createDir(dirName);
        String path="./uploadfiles/" + file.getOriginalFilename();

        File newFile=new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);

        String statusCode = qiniuUtils.uploadResouce(path, file.getOriginalFilename());
        System.out.println("status code: " + statusCode);

        long  endTime=System.currentTimeMillis();
        System.out.println("方法二的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "/success";
    }

    /*
     *采用spring提供的上传文件的方法
     */
    @RequestMapping("/uploadfile3")
    public String  springUpload(HttpServletRequest request) throws IllegalStateException, IOException
    {
        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String path="./"+file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));
                }
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "/success";
    }
}
