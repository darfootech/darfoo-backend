package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Music;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public class MusicDao {
    @Autowired
    private SessionFactory sf;

    //插入单个音频
    @SuppressWarnings("unchecked")
    public int insertSingleMusic(Music music) {
        Set<MusicCategory> mCategories = music.getCategories();
        try {
            Session session = sf.getCurrentSession();
            //对于MusicCategory，插入时默认认为全都属于MusicCategory表，所以只需找到对应种类的实体即可
            Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
            for (MusicCategory mCategory : mCategories) {
                s_title.add(mCategory.getTitle());
            }
            Criteria c = session.createCriteria(MusicCategory.class).add(Restrictions.in("title", s_title));
            List<MusicCategory> l_mCategory = c.list();
            mCategories = new HashSet<MusicCategory>(l_mCategory);
            music.setCategories(mCategories);
            session.save(music);
            int insertId = music.getId();
            return insertId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * update Music
     * 在update之前，请务必做updateMusicCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id             需要更新的对象对应的id
     * @param categoryTitles 种类的集合(null值表示不需要更新)
     */
    public int updateMusic(Integer id, String title, Set<String> categoryTitles, Long updateTimestamp) {
        int res;
        try {
            Session session = sf.getCurrentSession();
            Music oldMusic = (Music) session.get(Music.class, id);
            //默认认为种类值已经都在数据库中
            Set<MusicCategory> s_vCategories = oldMusic.getCategories();
            if (categoryTitles != null && categoryTitles.size() > 0) {
                //存在需要更新的种类,从表中获得对应的种类对象
                List<MusicCategory> l_Categories = session.createCriteria(MusicCategory.class).add(Restrictions.in("title", categoryTitles)).setReadOnly(true).list();
                if (l_Categories.size() > 0) {
                    s_vCategories.clear();//删除原始关联
                    oldMusic.setCategories(new HashSet<MusicCategory>(l_Categories)); //增加新的关联
                }
            } else {
                System.out.println("种类不需要更新");
            }
            if (updateTimestamp != null)
                oldMusic.setUpdate_timestamp(updateTimestamp);

            if (title != null) {
                oldMusic.setTitle(title);
            }

            System.out.println("-----更新后的Music如下-----");
            System.out.println(oldMusic.toString(true));
            session.saveOrUpdate(oldMusic);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }
}
