package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.cota.annotations.limit.HotSize;
import com.darfoo.backend.model.cota.annotations.limit.NewestSize;
import com.darfoo.backend.model.cota.annotations.limit.PageSize;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-2-20.
 */

//限制显示资源的数量 分页显示 热门资源个数
public class LimitDao {
    @Autowired
    CommonDao commonDao;

    /**
     * 获得某一类别资源的限制个数
     * 获得某一类别资源的每一页要显示的记录个数
     * 获得某一类别资源的热门资源的个数
     * 获取某一类别资源的最新资源的个数
     * @param resource
     * @param limit HotSize.class NewestSize.class PageSize.class
     * @return
     */
    public int getResourceLimitSize(Class resource, Class limit) {
        try {
            Method method = limit.getDeclaredMethod(limit.getSimpleName().toLowerCase());
            return (Integer) method.invoke(resource.getAnnotation(PageSize.class), new Object[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
