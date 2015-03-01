package com.darfoo.backend.caches.dao;

import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-3-1.
 */
public class CacheUtils {
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CategoryDao categoryDao;

    public Object cacheSingleResource(String type, Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        cacheDao.insertSingleResource(resource, object, type);

        return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, id));
    }

    public List cacheIndexResources(String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindex", type);

        List resources;
        if (type.equals("video")) {
            resources = commonDao.getResourcesByNewest(resource, 12);
        } else if (type.equals("author")) {
            resources = authorDao.getAuthorsOrderByVideoCountDesc();
        } else {
            System.out.println("wired");
            resources = new ArrayList();
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    public List cacheResourcesByCategories(String type, String categories) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%s", type, categories);

        List resources = categoryDao.getResourcesByCategories(resource, ServiceUtils.convertList2Array(cacheDao.parseResourceCategories(resource, categories)));
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SET);
    }

    public List cacheHottestResources(String type, Integer limit) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getResourcesByHottest(resource, limit);
        String cachekey = String.format("%shottest", type);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return cacheDao.extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
    }
}
