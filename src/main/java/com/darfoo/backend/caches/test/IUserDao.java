package com.darfoo.backend.caches.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author http://blog.csdn.net/java2000_wl 
 * @version <b>1.0</b> 
 */
public interface IUserDao {

    /**
     * 新增
     * <br>------------------------------<br>
     * @param user
     * @return
     */
    boolean add(User user);

    /**
     * 批量新增 使用pipeline方式
     * <br>------------------------------<br>
     * @param list
     * @return
     */
    boolean add(List<User> list);

    /**
     * 删除
     * <br>------------------------------<br>
     * @param key
     */
    void delete(String key);

    /**
     * 删除多个
     * <br>------------------------------<br>
     * @param keys
     */
    void delete(List<String> keys);

    /**
     * 修改
     * <br>------------------------------<br>
     * @param user
     * @return
     */
    boolean update(User user);

    /**
     * 通过key获取
     * <br>------------------------------<br>
     * @param keyId
     * @return
     */
    User get(String keyId);

    boolean deleteCurrentDB();

    public void hmset(String key, HashMap param);

    public Collection<String> hmget(String key, Collection<String> fields);

    public String hget(String key, String field);
}