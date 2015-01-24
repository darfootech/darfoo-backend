package com.darfoo.backend.dao;

import java.util.*;

import com.darfoo.backend.model.*;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unchecked")
public class DanceDao {
    @Autowired
    private SessionFactory sf;

    public void insertSingleDanceGroup(DanceGroup group) {
        DanceGroupImage image = group.getImage();
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(DanceGroupImage.class).add(Restrictions.eq("image_key", image.getImage_key()));
            c.setReadOnly(true);
            List<DanceGroupImage> l_image = c.list();
            if (l_image.size() > 0) {
                //图片库中包含此图片信息，用持久化对象代替原来的image
                group.setImage(l_image.get(0));
            }
            session.save(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据name判断该舞队是否已经存在表里
     *
     * @param name 待判断的舞队的name
     * @return 表中已经存在该name对应的舞队信息, 返回true;反之，返回一个false
     */
    public boolean isDanceGroupExists(String name) {
        boolean isExist = true;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from dancegroup where name=:name";
            DanceGroup danceGroup = (DanceGroup) session.createSQLQuery(sql).addEntity(DanceGroup.class).setString("name", name).uniqueResult();
            isExist = (danceGroup == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * 获取舞队信息
     *
     * @param count 需要返回的舞队数量
     * @return 返回舞队List
     * *
     */
    public List<DanceGroup> getDanceGroups(int count) {
        List<DanceGroup> l_dance = new ArrayList<DanceGroup>();
        ;
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(DanceGroup.class);
            c.setReadOnly(true);
            c.setMaxResults(count);
            l_dance = c.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l_dance;
    }

    /**
     * 根据id删除dancegroup
     * *
     */
    public int deleteDanceGroupById(Integer id) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            String sql = "delete from dancegroup where id=:id";
            res = session.createSQLQuery(sql).setInteger("id", id).executeUpdate();
            if (res > 0) {
                //删除成功
                res = CRUDEvent.DELETE_SUCCESS;
            } else {
                res = CRUDEvent.DELETE_NOTFOUND;
            }
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 更新舞队信息前先对被更新对象进行check(主要检查舞队图片是否已经存在数据库中)
     *
     * @param id        被更新的舞队的id
     * @param image_key 新的舞队图片的key(null值表示不需要更新)
     *                  *
     */
    public UpdateCheckResponse updateDanceGroupCheck(Integer id, String image_key) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        try {
            Session session = sf.getCurrentSession();
            DanceGroup danceGroup = (DanceGroup) session.get(DanceGroup.class, id);
            if (danceGroup == null) {
                System.out.println("要更新的舞队信息不存在");
                response.setDancegroupUpdate(1);
            } else {
                if (image_key != null) {
                    Criteria c = session.createCriteria(DanceGroupImage.class).add(Restrictions.eq("image_key", image_key)).setReadOnly(true);
                    DanceGroupImage image = (DanceGroupImage) c.uniqueResult();
                    if (image == null) {
                        System.out.println("舞队图片还未存在于数据库，请先插入舞队图片");
                        response.setGroupImageUpdate(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * update 舞队
     * 在update之前，请务必做updateDanceGroupCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
     * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
     *
     * @param id          需要更新的对象对应的id
     * @param authorname  新的作者名字(null值表示不需要更新)
     * @param description 新的图片key(null值表示不需要更新)
     * @param imagekey    新的图片key(null值表示不需要更新)
     *                    *
     */
    public int updateDanceGourp(Integer id, String name, String description, String imagekey, Long updateTimestamp) {
        int res = 0;
        try {
            Session session = sf.getCurrentSession();
            DanceGroup oldDanceGroup = (DanceGroup) session.get(DanceGroup.class, id);
            //check操作保证图片信息已经在dancegroupimage表中
            if (imagekey != null) {
                Criteria c = session.createCriteria(DanceGroupImage.class).add(Restrictions.eq("image_key", imagekey));
                DanceGroupImage image = (DanceGroupImage) c.uniqueResult();
                if (image != null) {
                    oldDanceGroup.setImage(image);
                } else {
                    return res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
                }
            } else {
                System.out.println("图片不需要更新");
            }
            if (name != null)
                oldDanceGroup.setName(name);
            if (description != null)
                oldDanceGroup.setDescription(description);
            if (updateTimestamp != null)
                oldDanceGroup.setUpdate_timestamp(updateTimestamp);
            res = CRUDEvent.UPDATE_SUCCESS;
            //session.saveOrUpdate(oldDanceGroup);oldDanceGroup本来就是持久化对象，所以save操作可有可无
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获得所有舞队
     *
     * @return Set<DanceGroup>
     * *
     */
    public List<DanceGroup> getAllDanceGourp() {
        List<DanceGroup> s_groups = new ArrayList<DanceGroup>();
        Map<Integer, DanceGroup> sortMap = new HashMap<Integer, DanceGroup>();

        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(DanceGroup.class).setReadOnly(true);
            List<DanceGroup> l_groups = c.list();
            if (l_groups.size() > 0) {
                for (DanceGroup danceGroup : l_groups) {
                    sortMap.put(danceGroup.getId(), danceGroup);
                }
            }

            List<Integer> sortedKeys = new ArrayList(sortMap.keySet());
            Collections.sort(sortedKeys);

            for (Integer index : sortedKeys) {
                s_groups.add(sortMap.get(index));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.reverse(s_groups);
        return s_groups;
    }

    public DanceGroup getTeamById(Integer id) {
        DanceGroup danceGroup = null;
        try {
            Session session = sf.getCurrentSession();
            String sql = "select * from dancegroup where id=:id";
            danceGroup = (DanceGroup) session.createSQLQuery(sql).addEntity(DanceGroup.class).setInteger("id", id).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danceGroup;
    }
}
