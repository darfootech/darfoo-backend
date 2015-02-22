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
        Author author = music.getAuthor();
        Image image = music.getImage();
        try {
            Session session = sf.getCurrentSession();
            //先查询image表中是否包含此music的图片信息
            Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", image.getImage_key()));//image的image_key字段需为unique
            List<Image> l_img = c.list();
            if (l_img.size() > 0) {
                //image表包含该video的图片信息,用持久化的实体代替原来的值
                music.setImage(l_img.get(0));
            }
            //查询author表中是否包含此music的作者信息
            c = session.createCriteria(Author.class).add(Restrictions.eq("name", author.getName())); //author的name字段需为unique
            List<Author> l_author = c.list();
            if (l_author.size() > 0) {
                //author表包含该video的作者信息,用持久化的实体代替原来的值
                music.setAuthor(l_author.get(0));
            }
            //对于MusicCategory，插入时默认认为全都属于MusicCategory表，所以只需找到对应种类的实体即可
            Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
            for (MusicCategory mCategory : mCategories) {
                s_title.add(mCategory.getTitle());
            }
            c = session.createCriteria(MusicCategory.class).add(Restrictions.in("title", s_title));
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
     * 获取单个music的信息
     * 根据music的title和作者id来获得music对象
     *
     * @return music 返回一个Music的实例对象(包含关联表中的数据)，详细请看Music.java类
     * *
     */
    public Music getMusicByTitleAuthorId(String title, int authorid) {
        Music music = null;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from music where title=:title and author_id=:authorid";
            music = (Music) session.createSQLQuery(sql).addEntity(Music.class).setString("title", title).setInteger("authorid", authorid).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return music;
    }

    /**
     * 根据类别获取歌曲列表(我要上网—音乐页面中)
     * $categories  (四拍-情歌风-A) 如果用户没有选择某个类别，那么就去掉该字符串
     * <p/>
     * param example -> categories = {"四拍","情歌风","A"}  例如 categories = {"四拍","A"} 表示有一个类别用户没有选择
     */
    public List<Music> getMusicsByCategories(String[] categories) {
        List<Music> l_music = new ArrayList<Music>();
        try {
            Session session = sf.getCurrentSession();
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的music id
            Criteria c;
            for (int i = 0; i < categories.length; i++) {
                c = session.createCriteria(Music.class).setProjection(Projections.property("id"));
                c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
                c.setReadOnly(true);
                List<Integer> l_id = c.list();
                System.out.println("满足条件 " + categories[i] + " 的music数量》》》" + l_id.size());
                for (Integer j : l_id) {
                    System.out.print(j + "#");
                }
                System.out.println();
                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Music>对象,长度为0
                    l_music = new ArrayList<Music>();
                    l_interact_id.clear();//清空
                    break;
                } else {
                    if (l_interact_id.size() == 0) {
                        l_interact_id = l_id;
                        continue;
                    } else {
                        l_interact_id.retainAll(l_id);
                        boolean hasItersection = l_interact_id.size() > 0 ? true : false;
                        if (!hasItersection) {
                            //之前查询的结果与当前的无交集，说明歌曲表无法满足该种类组合，返回一个空的List<Music>对象,长度为0
                            l_music = new ArrayList<Music>();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                c = session.createCriteria(Music.class);
                c.setReadOnly(true);
                l_music = c.list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                c = session.createCriteria(Music.class).add(Restrictions.in("id", l_interact_id));
                c.setReadOnly(true);
                l_music = c.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l_music;
    }

    /**
     * 更新Music之前先做check(主要是对author和image的check)
     * 不对title(key)进行update，这样的更新没有必要，不如直接插入一个Music(吉卉这个请注意)
     *
     * @param id         需要更新的对象对应的id
     * @param authorname 新的作者名字  (null值表示不需要更新)
     * @param imagekey   新的图片key (null值表示不需要更新)
     *                   *
     */
    public UpdateCheckResponse updateMusicCheck(Integer id, String authorname, String imagekey) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        Music oldMusic;
        try {
            Session session = sf.getCurrentSession();
            oldMusic = (Music) session.get(Music.class, id);
            if (oldMusic == null) {
                System.out.println("要更新的Music不存在");
                response.setMusicUpdate(1);
            } else {
                if (authorname != null) {
                    if (!authorname.equals(oldMusic.getAuthor().getName())) {
                        Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        c.setReadOnly(true);
                        Author a = (Author) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的Music的作者不存在，请先完成作者信息的插入");
                            response.setAuthorUpdate(1);
                        }
                    }
                }
                if (imagekey != null) {
                    if (!imagekey.equals(oldMusic.getImage().getImage_key())) {
                        Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                        c.setReadOnly(true);
                        Image a = (Image) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的Music的插图不存在，请完成图片插入");
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
     * update Music
     * 在update之前，请务必做updateMusicCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id             需要更新的对象对应的id
     * @param authorname     新的作者名字(null值表示不需要更新)
     * @param imagekey       新的图片key(null值表示不需要更新)
     * @param categoryTitles 种类的集合(null值表示不需要更新)
     *                       *
     */
    public int updateMusic(Integer id, String title, String authorname, String imagekey, Set<String> categoryTitles, Long updateTimestamp) {
        int res;
        try {
            Session session = sf.getCurrentSession();
            Music oldMusic = (Music) session.get(Music.class, id);
            //check操作保证作者信息已经在author表中
            if (authorname != null) {
                Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                Author author = (Author) c.uniqueResult();
                if (author != null) {
                    oldMusic.setAuthor(author);
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
                    oldMusic.setImage(image);
                } else {
                    return CRUDEvent.UPDATE_IMAGE_NOTFOUND;
                }
            } else {
                System.out.println("图片不需要更新");
            }
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
