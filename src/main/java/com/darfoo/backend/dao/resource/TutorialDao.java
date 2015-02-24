package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Tutorial;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TutorialDao {
    @Autowired
    private SessionFactory sf;

    /**
     * 更新Tutorial之前先做check(主要是对author和image的check)
     * ***不对title(key)进行update，这样的更新没有必要，不如直接插入一个Video(吉卉这个请注意)
     *
     * @param id         需要更新的对象对应的id
     * @param authorname 新的作者名字  (null值表示不需要更新)
     * @param imagekey   新的图片key (null值表示不需要更新)
     *                   *
     */
    public UpdateCheckResponse updateTutorialCheck(Integer id, String authorname, String imagekey) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        Tutorial oldTutorial;
        try {
            Session session = sf.getCurrentSession();
            oldTutorial = (Tutorial) session.get(Tutorial.class, id);
            if (oldTutorial == null) {
                System.out.println("要更新的Tutorial不存在");
                response.setTutorialUpdate(1);
            } else {
                if (authorname != null) {
                    if (!authorname.equals(oldTutorial.getAuthor().getName())) {
                        Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        c.setReadOnly(true);
                        Author a = (Author) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的Tutorial的作者不存在，请先完成作者信息的插入");
                            response.setAuthorUpdate(1);
                        }
                    }
                }
                if (imagekey != null) {
                    if (!imagekey.equals(oldTutorial.getImage().getImage_key())) {
                        Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                        c.setReadOnly(true);
                        Image a = (Image) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的Tutorial的插图不存在，请完成图片插入");
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
     * update Tutorial
     * 在update之前，请务必做updateTutorialCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id             需要更新的对象对应的id
     * @param authorname     新的作者名字(null值表示不需要更新)
     * @param imagekey       新的图片key(null值表示不需要更新)
     * @param categoryTitles 种类的集合(null值表示不需要更新)
     *                       *
     */
    public int updateTutorial(Integer id, String title, String authorname, String imagekey, Set<String> categoryTitles, Long updateTimestamp) {
        int res;
        try {
            Session session = sf.getCurrentSession();
            Tutorial oldTutorial = (Tutorial) session.get(Tutorial.class, id);
            //check操作保证作者信息已经在author表中
            if (authorname != null) {
                Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                Author author = (Author) c.uniqueResult();
                if (author != null) {
                    oldTutorial.setAuthor(author);
                } else {
                    return CRUDEvent.UPDATE_AUTHOR_NOTFOUND;
                }
            } else {
                System.out.println("作者不需要更新");
            }
            //check操作保证图片信息已经在image表中
            if (imagekey != null) {
                Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                Image image = (Image) c.uniqueResult();
                if (image != null) {
                    oldTutorial.setImage(image);
                } else {
                    return CRUDEvent.UPDATE_IMAGE_NOTFOUND;
                }
            } else {
                System.out.println("图片不需要更新");
            }
            //默认认为种类值已经都在数据库中
            Set<TutorialCategory> s_vCategories = oldTutorial.getCategories();
            if (categoryTitles != null && categoryTitles.size() > 0) {
                //存在需要更新的种类,从表中获得对应的种类对象
                List<TutorialCategory> l_Categories = session.createCriteria(TutorialCategory.class).add(Restrictions.in("title", categoryTitles)).setReadOnly(true).list();
                if (l_Categories.size() > 0) {
                    s_vCategories.clear();//删除原始关联
                    oldTutorial.setCategories(new HashSet<TutorialCategory>(l_Categories)); //增加新的关联
                }
            } else {
                System.out.println("种类不需要更新");
            }
            if (updateTimestamp != null)
                oldTutorial.setUpdate_timestamp(updateTimestamp);

            if (title != null) {
                oldTutorial.setTitle(title);
            }

            System.out.println("-----更新后的Tutorial如下-----");
            System.out.println(oldTutorial.toString(true));
            session.saveOrUpdate(oldTutorial);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }
}