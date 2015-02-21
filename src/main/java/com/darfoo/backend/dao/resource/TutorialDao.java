package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TutorialDao {
    @Autowired
    private SessionFactory sf;

    //插入单个education视频
    @SuppressWarnings("unchecked")
    public int insertSingleEducationVideo(Tutorial video) {
        Set<TutorialCategory> eCategories = video.getCategories();
        Author author = video.getAuthor();
        Image image = video.getImage();
        try {
            Session session = sf.getCurrentSession();
            //先查询image表中是否包含此video的图片信息
            Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", image.getImage_key()));//image的image_key字段需为unique
            List<Image> l_img = c.list();
            if (l_img.size() > 0) {
                //image表包含该video的图片信息,用持久化的实体代替原来的值
                video.setImage(l_img.get(0));
            }
            //查询author表中是否包含此video的作者信息
            c = session.createCriteria(Author.class).add(Restrictions.eq("name", author.getName())); //author的name字段需为unique
            List<Author> l_author = c.list();
            if (l_author.size() > 0) {
                //author表包含该video的作者信息,用持久化的实体代替原来的值
                video.setAuthor(l_author.get(0));
            }
            //对于VideoCategory，插入时默认认为全都属于VideoCategory表，所以只需找到对应种类的实体即可
            Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
            for (TutorialCategory eCategory : eCategories) {
                s_title.add(eCategory.getTitle());
            }
            c = session.createCriteria(TutorialCategory.class).add(Restrictions.in("title", s_title));
            List<TutorialCategory> l_vCategory = c.list();
            eCategories = new HashSet<TutorialCategory>(l_vCategory);
            video.setCategories(eCategories);
            session.saveOrUpdate(video);
            int insertId = video.getId();
            return insertId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 更新Education之前先做check(主要是对author和image的check)
     * ***不对title(key)进行update，这样的更新没有必要，不如直接插入一个Video(吉卉这个请注意)
     *
     * @param id         需要更新的对象对应的id
     * @param authorname 新的作者名字  (null值表示不需要更新)
     * @param imagekey   新的图片key (null值表示不需要更新)
     *                   *
     */
    public UpdateCheckResponse updateEducationCheck(Integer id, String authorname, String imagekey) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        Tutorial oldEducation = null;
        try {
            Session session = sf.getCurrentSession();
            oldEducation = (Tutorial) session.get(Tutorial.class, id);
            if (oldEducation == null) {
                System.out.println("要更新的Education不存在");
                response.setEducationUpdate(1);
            } else {
                if (authorname != null) {
                    if (!authorname.equals(oldEducation.getAuthor().getName())) {
                        Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        c.setReadOnly(true);
                        Author a = (Author) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的education的作者不存在，请先完成作者信息的插入");
                            response.setAuthorUpdate(1);
                        }
                    }
                }
                if (imagekey != null) {
                    if (!imagekey.equals(oldEducation.getImage().getImage_key())) {
                        Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                        c.setReadOnly(true);
                        Image a = (Image) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的education的插图不存在，请完成图片插入");
                            response.setImageUpdate(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * update Edcation
     * 在update之前，请务必做updateEducationCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id             需要更新的对象对应的id
     * @param authorname     新的作者名字(null值表示不需要更新)
     * @param imagekey       新的图片key(null值表示不需要更新)
     * @param categoryTitles 种类的集合(null值表示不需要更新)
     *                       *
     */
    public int updateEducation(Integer id, String title, String authorname, String imagekey, Set<String> categoryTitles, Long updateTimestamp) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Tutorial oldEducation = (Tutorial) session.get(Tutorial.class, id);
            //check操作保证作者信息已经在author表中
            if (authorname != null) {
                Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                Author author = (Author) c.uniqueResult();
                if (author != null) {
                    oldEducation.setAuthor(author);
                } else {
                    return res = CRUDEvent.UPDATE_AUTHOR_NOTFOUND;
                }
            } else {
                System.out.println("作者不需要更新");
            }
            //check操作保证图片信息已经在image表中
            if (imagekey != null) {
                Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                Image image = (Image) c.uniqueResult();
                if (image != null) {
                    oldEducation.setImage(image);
                } else {
                    return res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
                }
            } else {
                System.out.println("图片不需要更新");
            }
            //默认认为种类值已经都在数据库中
            Set<TutorialCategory> s_vCategories = oldEducation.getCategories();
            if (categoryTitles != null && categoryTitles.size() > 0) {
                //存在需要更新的种类,从表中获得对应的种类对象
                List<TutorialCategory> l_Categories = session.createCriteria(TutorialCategory.class).add(Restrictions.in("title", categoryTitles)).setReadOnly(true).list();
                if (l_Categories.size() > 0) {
                    s_vCategories.clear();//删除原始关联
                    oldEducation.setCategories(new HashSet<TutorialCategory>(l_Categories)); //增加新的关联
                }
            } else {
                System.out.println("种类不需要更新");
            }
            if (updateTimestamp != null)
                oldEducation.setUpdate_timestamp(updateTimestamp);

            if (title != null) {
                oldEducation.setTitle(title);
            }

            System.out.println("-----更新后的education如下-----");
            System.out.println(oldEducation.toString(true));
            session.saveOrUpdate(oldEducation);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 为education添加一个对应的music(插入操作用更新替代)
     *
     * @param vId 对应的视频资源id
     * @param mId music对应的id
     */
    public int insertOrUpdateMusic(Integer vId, Integer mId) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Tutorial education = (Tutorial) session.get(Tutorial.class, vId);
            if (education != null) {
                Music music = (Music) session.createCriteria(Music.class).add(Restrictions.eq("id", mId)).uniqueResult();
                if (music == null) {
                    System.out.println("被更新的music的id在music表中未发现对应记录，请先完成music的插入");
                    res = CRUDEvent.UPDATE_MUSIC_NOTFOUND;
                } else {
                    education.setMusic(music);
                    res = CRUDEvent.UPDATE_SUCCESS;
                }
            } else {
                System.out.println("vid对应的education未找到");
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            }

        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 删除education中的music(就是将MUSIC_ID字段设为null)
     *
     * @param vId education的Id
     *            *
     */
    public int deleteMusicFromTutorial(Integer vId) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Tutorial education = (Tutorial) session.get(Tutorial.class, vId);
            if (education != null) {
                if (!(education.getMusic() == null))
                    education.setMusic(null);
                else {
                    System.out.println("music_id 已经为null");
                }
                res = CRUDEvent.DELETE_SUCCESS;
            } else {
                res = CRUDEvent.DELETE_NOTFOUND;
            }
        } catch (Exception e) {
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }

    public void disconnectTutorialMusic(int videoid, int musicid) {
        System.out.println(CRUDEvent.getResponse(insertOrUpdateMusic(videoid, musicid)));
        System.out.println(CRUDEvent.getResponse(deleteMusicFromTutorial(videoid)));
    }
}
