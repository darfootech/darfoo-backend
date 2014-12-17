package com.darfoo.backend.caches.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import redis.clients.jedis.Jedis;

/**
 * Dao
 * @author http://blog.csdn.net/java2000_wl 
 * @version <b>1.0</b> 
 */
public class UserDao extends AbstractBaseRedisDao<String, User> implements IUserDao {

    /**
     * 新增
     *<br>------------------------------<br>
     * @param user
     * @return
     */
    public boolean add(final User user) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(user.getId());
                byte[] name = serializer.serialize(user.getName());
                return connection.setNX(key, name);
            }
        });
        return result;
    }

    /**
     * 批量新增 使用pipeline方式
     *<br>------------------------------<br>
     *@param list
     *@return
     */
    public boolean add(final List<User> list) {
        Assert.notEmpty(list);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                for (User user : list) {
                    byte[] key  = serializer.serialize(user.getId());
                    byte[] name = serializer.serialize(user.getName());
                    connection.setNX(key, name);
                }
                return true;
            }
        }, false, true);
        return result;
    }

    /**
     * 删除
     * <br>------------------------------<br>
     * @param key
     */
    public void delete(String key) {
        List<String> list = new ArrayList<String>();
        list.add(key);
        delete(list);
    }

    /**
     * 删除多个
     * <br>------------------------------<br>
     * @param keys
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 修改
     * <br>------------------------------<br>
     * @param user
     * @return
     */
    public boolean update(final User user) {
        String key = user.getId();
        if (get(key) == null) {
            throw new NullPointerException("数据行不存在, key = " + key);
        }
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(user.getId());
                byte[] name = serializer.serialize(user.getName());
                connection.set(key, name);
                return true;
            }
        });
        return result;
    }

    /**
     * 通过key获取
     * <br>------------------------------<br>
     * @param keyId
     * @return
     */
    public User get(final String keyId) {
        User result = redisTemplate.execute(new RedisCallback<User>() {
            public User doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key = serializer.serialize(keyId);
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                String name = serializer.deserialize(value);
                return new User(keyId, name, null);
            }
        });
        return result;
    }

    public void deleteCurrentDB(){
        Properties properties = new Properties();

        String host = "";
        String auth = "";
        int port = -1;
        int timeout = -1;

        try {
            properties.load(UserDao.class.getClassLoader().getResourceAsStream("redis.properties"));
            host = properties.getProperty("redis.host");
            auth = properties.getProperty("redis.pass");
            port = Integer.parseInt(properties.getProperty("redis.port"));
            timeout = Integer.parseInt(properties.getProperty("redis.maxWait"));
            System.out.println(host);
            System.out.println(auth);
            System.out.println(port);
            System.out.println(timeout);
        }catch (Exception e){
            e.printStackTrace();
        }

        Jedis jedis = new Jedis(host, port, timeout);
        if (!auth.equals("")){
            jedis.auth(auth);
        }

        try {
            jedis.flushDB();
        }catch (Exception e){
            System.out.println("jedis java socket time out");
        }finally {
            jedis.close();
        }
    }
}