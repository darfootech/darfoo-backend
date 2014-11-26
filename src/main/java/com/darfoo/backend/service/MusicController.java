package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.SearchDao;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.service.responsemodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resources/music")
public class MusicController {
    @Autowired
    MusicDao musicDao;
    @Autowired
    SearchDao searchDao;

    MusicCates musicCates = new MusicCates();

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleMusic getSingleMusic(@PathVariable String id){
        Music targetMusic = musicDao.getMusicByMusicId(Integer.parseInt(id));
        int music_id = targetMusic.getId();
        String music_url = targetMusic.getMusic_key();
        String title = targetMusic.getTitle();
        return new SingleMusic(music_id, music_url, title);
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/search/s
    @RequestMapping(value = "/search/{content}", method = RequestMethod.GET)
    public @ResponseBody
    List<SearchMusic> searchMusic(@PathVariable String content){
        List<Music> musics = searchDao.getMusicBySearch(content);
        List<SearchMusic> result = new ArrayList<SearchMusic>();
        for (Music music : musics){
            int id = music.getId();
            String title = music.getTitle();
            Long update_timestamp = music.getUpdate_timestamp();
            result.add(new SearchMusic(id, title, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/hottest")
    public @ResponseBody
    List<HotMusic> getHottestMusics(){
        List<Music> musics = musicDao.getHottestMusics(5);
        List<HotMusic> result = new ArrayList<HotMusic>();
        for (Music music : musics){
            int id = music.getId();
            String title = music.getTitle();
            Long update_timestamp = music.getUpdate_timestamp();
            result.add(new HotMusic(id, title, update_timestamp));
        }
        return result;
    }

    String[] convertList2Array(List<String> vidoes) {
        String[] stockArr = new String[vidoes.size()];
        stockArr = vidoes.toArray(stockArr);
        return stockArr;
    }

    //http://localhost:8080/darfoobackend/rest/resources/music/category/1-0-0
    @RequestMapping(value = "/category/{categories}", method = RequestMethod.GET)
    public @ResponseBody
    List<CategoryMusic> getMusicByCategories(@PathVariable String categories){
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

        List<Music> musics = musicDao.getMusicsByCategories(convertList2Array(targetCategories));
        List<CategoryMusic> result = new ArrayList<CategoryMusic>();
        for (Music music : musics){
            int music_id = music.getId();
            String image_url = music.getImage().getImage_key();
            String title = music.getTitle();
            Long update_timestamp = music.getUpdate_timestamp();
            result.add(new CategoryMusic(music_id, image_url, title, update_timestamp));
        }
        return result;
    }
}
