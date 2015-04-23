package com.darfoo.backend;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.LimitDao;
import com.darfoo.backend.model.cota.annotations.limit.PageSize;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zjh on 15-4-2.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class CotaTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    LimitDao limitDao;

    @Test
    public void getEnumValueFromMetaData() {
        String[] fields = {"hot", "priority"};
        for (String field : fields) {
            //negative value
            Enum negativevalue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("NOT%s", field.toUpperCase()));
            System.out.println(negativevalue);
            //positive value
            Enum positivevalue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("IS%s", field.toUpperCase()));
            System.out.println(positivevalue);
        }
    }

    @Test
    public void getValueFromAnnotation() {
        Class resource = DanceVideo.class;
        Class limit = PageSize.class;
        try {
            Method method = limit.getDeclaredMethod(limit.getSimpleName().toLowerCase());
            System.out.println(method.invoke(resource.getAnnotation(limit), new Object[]{}));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(limitDao.getResourceLimitSize(resource, limit));
    }
}
