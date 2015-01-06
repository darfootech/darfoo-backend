package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.SearchDao;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.service.responsemodel.*;
import com.darfoo.backend.utils.QiniuUtils;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resources/music")
public class MusicController {
    @Autowired
    MusicDao musicDao;
    @Autowired
    SearchDao searchDao;

    QiniuUtils qiniuUtils = new QiniuUtils();
    MusicCates musicCates = new MusicCates();

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleMusic getSingleMusic(@PathVariable String id){
        Music targetMusic = musicDao.getMusicByMusicId(Integer.parseInt(id));
        int music_id = targetMusic.getId();
        String music_url = targetMusic.getMusic_key() + ".mp3";
        //String image_url = targetMusic.getImage().getImage_key();
        String music_download_url = qiniuUtils.getQiniuResourceUrl(music_url);
        //String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
        String title = targetMusic.getTitle();
        /*String author_name = "";
        if (targetMusic.getAuthor() != null){
            author_name = targetMusic.getAuthor().getName();
        }*/
        String author_name = targetMusic.getAuthorName();
        long update_timestamp = targetMusic.getUpdate_timestamp();
        //return new SingleMusic(music_id, music_download_url, image_download_url, author_name, title);
        return new SingleMusic(music_id, title, author_name, music_download_url, update_timestamp);
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/search?search=s
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleMusic> searchMusic(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Music> musics = searchDao.getMusicBySearch(searchContent);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int id = music.getId();
            String title = music.getTitle();
            /*String author_name = "";
            if (music.getAuthor() != null){
                author_name = music.getAuthor().getName();
            }*/
            String author_name = music.getAuthorName();
            //String image_url = music.getImage().getImage_key();
            //String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String music_url = music.getMusic_key() + ".mp3";
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music_url);
            Long update_timestamp = music.getUpdate_timestamp();
            //result.add(new SearchMusic(id, title, image_download_url, music_download_url, author_name, update_timestamp));
            result.add(new SingleMusic(id, title, author_name, music_download_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/hottest")
    public @ResponseBody
    List<SingleMusic> getHottestMusics(){
        List<Music> musics = musicDao.getMusicsByHottest(5);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int id = music.getId();
            String title = music.getTitle();
            /*String author_name = "";
            if (music.getAuthor() != null){
                author_name = music.getAuthor().getName();
            }*/
            String author_name = music.getAuthorName();
            String music_url = music.getMusic_key() + ".mp3";
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music_url);
            Long update_timestamp = music.getUpdate_timestamp();
            result.add(new SingleMusic(id, title, author_name, music_download_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping(value = "/all/service", method = RequestMethod.GET)
    public @ResponseBody
    List<HashMap<String, Object>> getAllMusicService(){
        List<Music> musics = musicDao.getAllMusic();
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        for (Music music : musics){
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("word", music.getTitle() + "-" + music.getAuthorName() + "-" + music.getId());
            result.add(item);
        }
        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/category/1-0-0
    @RequestMapping(value = "/category/{categories}", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleMusic> getMusicByCategories(@PathVariable String categories){
        //System.out.println("category request is " + categories + " !!!!!!!!!");
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")){
            String beatCate = musicCates.getBeatCategory().get(requestCategories[0]);
            targetCategories.add(beatCate);
        }
        if (!requestCategories[1].equals("0")){
            String styleCate = musicCates.getStyleCategory().get(requestCategories[1]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[2].equals("0")){
            String letterCate = requestCategories[2];
            targetCategories.add(letterCate);
        }

        List<Music> musics = musicDao.getMusicsByCategories(ServiceUtils.convertList2Array(targetCategories));
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics){
            int music_id = music.getId();
            //String image_url = music.getImage().getImage_key();
            //String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String title = music.getTitle();
            /*String author_name = "";
            if (music.getAuthor() != null){
                author_name = music.getAuthor().getName();
            }*/
            String author_name = music.getAuthorName();
            String music_url = music.getMusic_key() + ".mp3";
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music_url);
            Long update_timestamp = music.getUpdate_timestamp();
            //result.add(new CategoryMusic(music_id, image_download_url, music_download_url, title, author_name, update_timestamp));
            result.add(new SingleMusic(music_id, title, author_name, music_download_url, update_timestamp));
        }
        return result;
    }
}
