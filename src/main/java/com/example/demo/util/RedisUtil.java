package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //region CacheObject 缓存基本对象

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
        return setCacheObject(key, value, 0);
    }

    public <T> ValueOperations<String, T> setCacheObject(String key, T value, long time_second) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        if (time_second > 0) redisTemplate.expire(key, time_second, TimeUnit.SECONDS);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(String key) {
        try {
            ValueOperations<String, T> operation = redisTemplate.opsForValue();
            return operation.get(key);
        } catch (Exception ex) {
            return null;
        }
    }
    //endregion

    //region CacheList 缓存List，取出的时候，会删除原缓存数据

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        return setCacheList(key, dataList, 0);
    }

    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, long time_second) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                listOperation.rightPush(key, dataList.get(i));
            }
        }
        if (time_second > 0) redisTemplate.expire(key, time_second, TimeUnit.SECONDS);

        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add((T) listOperation.leftPop(key));
        }

        return dataList;
    }
    //endregion


    //region CacheMap 缓存Map

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {
        return setCacheMap(key, dataMap, 0);
    }

    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap, long time_second) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                /*System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); */
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
            if (time_second > 0) redisTemplate.expire(key, time_second, TimeUnit.SECONDS);
        }
        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key) {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
        /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }
    //endregion

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    //region 批量(模糊)处理方法

    /**
     * 根据关键字获取相应的key
     *
     * @param key 关键字
     * @return
     */
    public List<Object> batchGet(String key) {
        //redis的keys(*)指令业务量大时，会影响业务功能,生产环境禁用该指令
        //Set<String> keys = redisTemplate.keys("*" + key + "*");

        // 获取Redis中特定前缀的key
        Set<String> keys = scan(key);
        // 装换成list
        List<Object> list = new ArrayList(keys);

        return !list.isEmpty() ? list : null;
    }

    /**
     * scan 实现
     *
     * @param pattern 表达式，如：abc*，找出所有以abc开始的键
     */
    public Set<String> scan(String pattern) {
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match("*" + pattern + "*")
                    .count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
            return keysTmp;
        });
    }

    /**
     * 根据关键字模糊删除相关的key
     *
     * @param key 关键字
     * @return
     */
    public void batchDel(String key) {
        new Thread(() -> {
            // 获取Redis中包含关键字的key
            Set<String> keys = scan(key);
            if (keys != null) {
                // 删除这些key
                redisTemplate.delete(keys);
            }
        }).start();
    }
    //endregion

}

