package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
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
public class VideoDao {
    @Autowired
    private SessionFactory sf;
    @Autowired
    CommonDao commonDao;

    private int pageSize = 12;

    //插入单个视频
    @SuppressWarnings("unchecked")
    public int insertSingleVideo(Video video) {
        Set<VideoCategory> vCategories = video.getCategories();
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
            for (VideoCategory vCategory : vCategories) {
                s_title.add(vCategory.getTitle());
            }
            c = session.createCriteria(VideoCategory.class).add(Restrictions.in("title", s_title));
            List<VideoCategory> l_vCategory = c.list();
            vCategories = new HashSet<VideoCategory>(l_vCategory);
            video.setCategories(vCategories);
            session.save(video);
            int insertId = video.getId();
            return insertId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据类别获取视频列表(我要上网—视频页面)
     * $categories  (较快-简单—欢快-A) 如果用户没有选择某个类别，那么就去掉该字符串
     * <p/>
     * params example -> categories = {"较快","简单","欢快","A"}  例如 categories = {"较快","欢快","A"} 表示有一个类别用户没有选择
     */
    public List<Video> getVideosByCategories(String[] categories) {
        List<Video> l_video = new ArrayList<Video>();
        try {
            Session session = sf.getCurrentSession();
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            Criteria c;
            for (int i = 0; i < categories.length; i++) {
                c = session.createCriteria(Video.class).setProjection(Projections.property("id"));
                c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
                //这个降序的机制在这里木有用
                //c.addOrder(Order.desc("id"));
                c.setReadOnly(true);
                List<Integer> l_id = c.list();
                System.out.println("满足条件 " + categories[i] + " 的video数量》》》" + l_id.size());
                for (Integer j : l_id) {
                    System.out.print(j + "#");
                }
                System.out.println();
                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                    l_video = new ArrayList<Video>();
                    l_interact_id.clear();//清空，表示无交集
                    break;
                } else {
                    if (l_interact_id.size() == 0) {
                        l_interact_id = l_id;
                        continue;
                    } else {
                        l_interact_id.retainAll(l_id);
                        boolean hasItersection = l_interact_id.size() > 0 ? true : false;
                        if (!hasItersection) {
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                            l_video = new ArrayList<Video>();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                c = session.createCriteria(Video.class);
                //c.addOrder(Order.desc("id"));
                c.setReadOnly(true);
                l_video = c.list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                c = session.createCriteria(Video.class).add(Restrictions.in("id", l_interact_id));
                //c.addOrder(Order.desc("id"));
                c.setReadOnly(true);
                l_video = c.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Collections.reverse(l_video);
        return l_video;
    }

    /**
     * 更新Video之前先做check(主要是对author和image的check)
     *
     * @param id         需要更新的对象对应的id
     * @param authorname 新的作者名字  (null值表示不需要更新)
     * @param imagekey   新的图片key (null值表示不需要更新)
     * @return response  里面包含check的结果
     * *
     */
    public UpdateCheckResponse updateVideoCheck(Integer id, String authorname, String imagekey) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        Video oldVideo = null;
        try {
            Session session = sf.getCurrentSession();
            oldVideo = (Video) session.get(Video.class, id);
            if (oldVideo == null) {
                System.out.println("要更新的Video不存在");
                response.setVideoUpdate(1);
            } else {
                if (authorname != null) {
                    if (!authorname.equals(oldVideo.getAuthor().getName())) {
                        Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        c.setReadOnly(true);
                        Author a = (Author) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的video的作者不存在，请先完成作者信息的插入");
                            response.setAuthorUpdate(1);
                        }
                    }
                }
                if (imagekey != null) {
                    if (!imagekey.equals(oldVideo.getImage().getImage_key())) {
                        Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                        c.setReadOnly(true);
                        Image a = (Image) c.uniqueResult();
                        if (a == null) {
                            System.out.println("要更新的video的插图不存在，请完成图片插入");
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
     * update Video
     * 在update之前，请务必做updateVideoCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id             需要更新的对象对应的id
     * @param authorname     新的作者名字(null值表示不需要更新)
     * @param imagekey       新的图片key(null值表示不需要更新)
     * @param categoryTitles 种类的集合(null值表示不需要更新)
     *                       *
     */
    public int updateVideo(Integer id, String title, String authorname, String imagekey, Set<String> categoryTitles, Long updateTimestamp) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Video oldVideo = (Video) session.get(Video.class, id);
            //check操作保证作者信息已经在author表中
            if (authorname != null) {
                Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                Author author = (Author) c.uniqueResult();
                if (author != null) {
                    oldVideo.setAuthor(author);
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
                    oldVideo.setImage(image);
                } else {
                    return res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
                }
            } else {
                System.out.println("图片不需要更新");
            }
            //默认认为种类值已经都在数据库中
            Set<VideoCategory> s_vCategories = oldVideo.getCategories();
            if (categoryTitles != null && categoryTitles.size() > 0) {
                //存在需要更新的种类,从表中获得对应的种类对象
                List<VideoCategory> l_Categories = session.createCriteria(VideoCategory.class).add(Restrictions.in("title", categoryTitles)).setReadOnly(true).list();
                if (l_Categories.size() > 0) {
                    s_vCategories.clear();//删除原始关联
                    oldVideo.setCategories(new HashSet<VideoCategory>(l_Categories)); //增加新的关联
                }
            } else {
                System.out.println("种类不需要更新");
            }
            if (updateTimestamp != null)
                oldVideo.setUpdate_timestamp(updateTimestamp);

            if (title != null) {
                oldVideo.setTitle(title);
            }

            System.out.println("-----更新后的video如下-----");
            System.out.println(oldVideo.toString(true));
            session.saveOrUpdate(oldVideo);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 为video添加一个对应的music(插入操作用更新替代)
     *
     * @param vId 对应的视频资源id
     * @param mId music对应的id
     */
    public int insertOrUpdateMusic(Integer vId, Integer mId) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Video video = (Video) session.get(Video.class, vId);
            if (video != null) {
                Music music = (Music) session.createCriteria(Music.class).add(Restrictions.eq("id", mId)).uniqueResult();
                if (music == null) {
                    System.out.println("被更新的music的id在music表中未发现对应记录，请先完成music的插入");
                    res = CRUDEvent.UPDATE_MUSIC_NOTFOUND;
                } else {
                    video.setMusic(music);
                    res = CRUDEvent.UPDATE_SUCCESS;
                }
            } else {
                System.out.println("vid对应的video未找到");
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            }

        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取video对应的music
     *
     * @param vId 对应的视频资源id
     * @return music对应的Music;没有就为null
     * *
     */
    public Music getMusic(Integer vId) {
        Music music = null;
        try {
            Session session = sf.getCurrentSession();
            Video video = (Video) session.get(Video.class, vId);
            if (video != null) {
                music = video.getMusic();
                if (music != null) {
                    music.trigLazyLoad(); //促发对category的加载
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return music;
    }

    /**
     * 删除video中的music(就是将MUSIC_ID字段设为null)
     *
     * @param vId video的Id
     *            *
     */
    public int deleteMusicFromVideo(Integer vId) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            Video video = (Video) session.get(Video.class, vId);
            if (video != null) {
                if (!(video.getMusic() == null))
                    video.setMusic(null);
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
     * 根据musicid来获得不与之关联的所有video
     *
     * @param musicid
     * @return
     */
    public List<Video> getVideosWithoutMusicId(int musicid) {
        List<Video> videos = null;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from video where music_id is null union select * from video where music_id <> :musicid order by id desc";
            videos = (List<Video>) session.createSQLQuery(sql).addEntity(Video.class).setInteger("musicid", musicid).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }

    public void disconnectVideoMusic(int videoid, int musicid) {
        System.out.println(CRUDEvent.getResponse(insertOrUpdateMusic(videoid, musicid)));
        System.out.println(CRUDEvent.getResponse(deleteMusicFromVideo(videoid)));
    }
}
