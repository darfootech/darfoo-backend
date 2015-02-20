package com.darfoo.backend.dao;

import com.darfoo.backend.model.*;
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
    @Autowired
    CommonDao commonDao;

    private int pageSize = 12;

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
     * 获取单个tutorial的信息
     * 根据tutorial的title和作者id来获得tutorial对象
     *
     * @return education 返回一个Education的实例对象(包含关联表中的数据)，详细请看Education.java类
     * *
     */
    public Tutorial getEducationByTitleAuthorId(String title, int authorid) {
        Tutorial education = null;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from education where title=:title and author_id=:authorid";
            education = (Tutorial) session.createSQLQuery(sql).addEntity(Tutorial.class).setString("title", title).setInteger("authorid", authorid).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return education;
    }

    /**
     * 根据类别获取视频列表(我要上网—视频页面) 暂时去掉名师那个种类
     * $categories  (快-简单—队形表演) 如果用户没有选择某个类别，那么就去掉该字符串
     * <p/>
     * param example -> categories = {"快","简单","队形表演"}  例如 categories = {"快","简单"}
     */
    public List<Tutorial> getEducationVideosByCategories(String[] categories) {
        List<Tutorial> l_video = new ArrayList<Tutorial>();
        try {
            Session session = sf.getCurrentSession();
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            Criteria c;
            for (int i = 0; i < categories.length; i++) {
                c = session.createCriteria(Tutorial.class).setProjection(Projections.property("id"));
                c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
                c.setReadOnly(true);
                List<Integer> l_id = c.list();
                System.out.println("满足条件 " + categories[i] + " 的enducation video数量》》》" + l_id.size());
                for (Integer j : l_id) {
                    System.out.print(j + "#");
                }
                System.out.println();
                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                    l_video = new ArrayList<Tutorial>();
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
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                            l_video = new ArrayList<Tutorial>();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                c = session.createCriteria(Tutorial.class);
                c.setReadOnly(true);
                l_video = c.list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                c = session.createCriteria(Tutorial.class).add(Restrictions.in("id", l_interact_id));
                c.setReadOnly(true);
                l_video = c.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l_video;
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

    public int updateVideoKeyById(int tutorialid, String newTutorialKey) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Tutorial oldTutorial = (Tutorial) session.get(Tutorial.class, tutorialid);
            oldTutorial.setVideo_key(newTutorialKey);
            session.saveOrUpdate(oldTutorial);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    public List<Tutorial> getAllTutorialsWithoutId(Integer tutorialid) {
        List<Tutorial> s_tutorials = new ArrayList<Tutorial>();
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Tutorial.class);
            c.add(Restrictions.not(Restrictions.eq("id", tutorialid)));
            c.addOrder(Order.desc("id"));
            c.setReadOnly(true);
            c.setFetchMode("categories", FetchMode.JOIN);
            List<Tutorial> l_tutorials = c.list();
            if (l_tutorials.size() > 0) {
                //去除重复的video
                for (Tutorial tutorial : l_tutorials) {
                    if (!s_tutorials.contains(tutorial)) {
                        s_tutorials.add(tutorial);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s_tutorials;
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
     * 获取education对应的music
     *
     * @param vId 对应的视频资源id
     * @return music对应的Music;没有就返回null
     * *
     */
    public Music getMusic(Integer vId) {
        Music music = null;
        try {
            Session session = sf.getCurrentSession();
            Tutorial education = (Tutorial) session.get(Tutorial.class, vId);
            if (education != null) {
                music = education.getMusic();
                music.trigLazyLoad(); //促发对category的加载
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return music;
    }

    /**
     * 删除education中的music(就是将MUSIC_ID字段设为null)
     *
     * @param vId education的Id
     *            *
     */
    public int deleteMusicFromEducation(Integer vId) {
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

   /**
     * 按热度排序，从热度最大到最小排序返回
     * <p/>
     * param 获得热度排名前number个
     */
    public List<Tutorial> getEducationsByHottest(int number) {
        List<Tutorial> educations = new ArrayList<Tutorial>();
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Tutorial.class);
            c.addOrder(Order.desc("hottest"));//安热度递减排序
            c.setMaxResults(number);
            c.setReadOnly(true);
            educations = c.list();
            for (Tutorial e : educations) {
                e.trigLazyLoad();   //强制触发延迟加载,避免Session关闭后再加载出现错误
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return educations;
    }

    /**
     * 获得最新的number个教学视频
     * <p/>
     * param 获得排名前number个
     */
    public List<Tutorial> getEducationsByNewest(int number) {
        List<Tutorial> educations = new ArrayList<Tutorial>();
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Tutorial.class);
            c.addOrder(Order.desc("update_timestamp"));//按最新时间排序
            c.setMaxResults(number);
            c.setReadOnly(true);
            educations = c.list();
            for (Tutorial e : educations) {
                e.trigLazyLoad();   //强制触发延迟加载,避免Session关闭后再加载出现错误
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return educations;
    }

    /**
     * 根据authorid来获得与之关联的所有tutorial
     *
     * @param authorid
     * @return
     */
    public List<Tutorial> getTutorialsByAuthorId(int authorid) {
        List<Tutorial> tutorials = null;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from education where author_id=:authorid order by id desc";
            tutorials = (List<Tutorial>) session.createSQLQuery(sql).addEntity(Tutorial.class).setInteger("authorid", authorid).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tutorials;
    }

    public void disconnectTutorialMusic(int videoid, int musicid) {
        System.out.println(CRUDEvent.getResponse(insertOrUpdateMusic(videoid, musicid)));
        System.out.println(CRUDEvent.getResponse(deleteMusicFromEducation(videoid)));
    }

    /*分页机制*/
    public long getPageCountByCategories(String[] categories) {
        List<Tutorial> l_video = new ArrayList<Tutorial>();
        try {
            Session session = sf.getCurrentSession();
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            Criteria c;
            for (int i = 0; i < categories.length; i++) {
                c = session.createCriteria(Tutorial.class).setProjection(Projections.property("id"));
                c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
                c.setReadOnly(true);
                List<Integer> l_id = c.list();
                System.out.println("满足条件 " + categories[i] + " 的enducation video数量》》》" + l_id.size());
                for (Integer j : l_id) {
                    System.out.print(j + "#");
                }
                System.out.println();
                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                    l_video = new ArrayList<Tutorial>();
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
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                            l_video = new ArrayList<Tutorial>();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                c = session.createCriteria(Tutorial.class);
                c.setReadOnly(true);
                l_video = c.list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                c = session.createCriteria(Tutorial.class).add(Restrictions.in("id", l_interact_id));
                c.setReadOnly(true);
                l_video = c.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (l_video.size() / pageSize) + 1;
    }

    public List<Tutorial> getTutorialsByCategoriesByPage(String[] categories, int pageNo) {
        List<Tutorial> l_video = new ArrayList<Tutorial>();

        if (pageNo > getPageCountByCategories(categories)) {
            return l_video;
        }

        try {
            Session session = sf.getCurrentSession();
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            Criteria c;
            for (int i = 0; i < categories.length; i++) {
                c = session.createCriteria(Tutorial.class).setProjection(Projections.property("id"));
                c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));

                /*分页机制*/
                c.setFirstResult((pageNo - 1) * pageSize);
                c.setMaxResults(pageSize);

                c.addOrder(Order.desc("id"));

                c.setReadOnly(true);
                List<Integer> l_id = c.list();
                System.out.println("满足条件 " + categories[i] + " 的enducation video数量》》》" + l_id.size());
                for (Integer j : l_id) {
                    System.out.print(j + "#");
                }
                System.out.println();
                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                    l_video = new ArrayList<Tutorial>();
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
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
                            l_video = new ArrayList<Tutorial>();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                c = session.createCriteria(Tutorial.class);
                c.setFirstResult((pageNo - 1) * pageSize);
                c.setMaxResults(pageSize);
                c.addOrder(Order.desc("id"));
                c.setReadOnly(true);
                l_video = c.list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                c = session.createCriteria(Tutorial.class).add(Restrictions.in("id", l_interact_id));
                c.setFirstResult((pageNo - 1) * pageSize);
                c.setMaxResults(pageSize);
                c.addOrder(Order.desc("id"));
                c.setReadOnly(true);
                l_video = c.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.reverse(l_video);
        return l_video;
    }

    /**
     * 获得播放页面右侧的相关教程
     * 获取原则 ->
     * 从相同明星舞队中随机选取5个
     * 如果相同明星舞队中教程个数不足则从所有教程中随机选出对应个数来填充
     *
     * @param tutorialid
     * @return
     */
    public List<Tutorial> getSideBarTutorials(Integer tutorialid) {
        List<Tutorial> result = new ArrayList<Tutorial>();
        int authorid = ((Tutorial) commonDao.getResourceById(Tutorial.class, tutorialid)).getAuthor().getId();
        List<Tutorial> sameAuthorTutorials = getTutorialsByAuthorId(authorid);
        int sameAuthorLen = sameAuthorTutorials.size();
        if (sameAuthorLen > 5) {
            Collections.shuffle(sameAuthorTutorials);
            for (int i = 0; i < 5; i++) {
                result.add(sameAuthorTutorials.get(i));
            }
        } else if (sameAuthorLen == 5) {
            result = sameAuthorTutorials;
        } else {
            List<Tutorial> allTutorials = getAllTutorialsWithoutId(tutorialid);
            Collections.shuffle(allTutorials);
            for (int i = 0; i < 5 - sameAuthorLen; i++) {
                sameAuthorTutorials.add(allTutorials.get(i));
            }
            result = sameAuthorTutorials;
        }
        return result;
    }
}
