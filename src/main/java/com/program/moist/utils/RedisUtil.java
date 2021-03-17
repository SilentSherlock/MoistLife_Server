package com.program.moist.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/3/16
 * Author: SilentSherlock
 * Description: operate redis
 */
@Slf4j
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    private static final String TAG = "RedisUtil-";
    private static final Long Default_TIME = (long) 60*60*24*7;

    //region key operations
    /**
     * 设置key持续时间
     * @param key 键
     * @param time 时间（秒）
     * @return
     */
    public static boolean expire(String key, Long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    public static boolean expire(String key, Long time, TimeUnit timeUnit) {
        if (time <= 0 || null == key || "".equals(key)) {
            return false;
        }
        try {
            redisTemplate.expire(key, time, timeUnit);
            return true;
        } catch (NullPointerException e) {
            log.error("RedisUtil-expire-空指针异常");
            return false;
        }
    }

    /**
     * 获得key的持续时间
     * @param key
     * @return
     */
    public static Long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    public static Long getExpire(String key, TimeUnit timeUnit) {
        if (null == key || "".equals(key)) {
            log.warn(TAG + "getExpire-key is null or empty string");
            return null;
        }

        Long time;
        try {
            time = redisTemplate.getExpire(key, timeUnit);
        } catch (NullPointerException e) {
            log.error(TAG + "getExpire-空指针异常");
            return null;
        }

        return time;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "hasKey-key is null or empty string");
            return false;
        }

        try {
            return redisTemplate.hasKey(key);
        } catch (NullPointerException e) {
            log.error(TAG + "hasKey-空指针异常");
            return false;
        }
    }

    /**
     * 根据给出的一个或多个key进行删除
     * @param key 应至少给出一个key
     * @return 删除个数
     */
    @SuppressWarnings("unchecked")
    public static Long delete(String... key) {
        if (key == null || key.length == 0) {
            log.warn(TAG + "delete-key is null or empty");
            return null;
        }

        try {
            if (key.length == 1) {
                Boolean res = redisTemplate.delete(key[0]);
                return res != null && res ? (long) 1 : (long) 0;
            }else {
                return redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        } catch (Exception e) {
            log.error(TAG + "delete got exception");
            return null;
        }
    }
    //endregion

    //region common key-value ops

    /**
     * 普通键值对取
     * @param key
     * @return
     */
    public static Object getCommon(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getCommon-key is null or empty");
            return null;
        }

        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(TAG + "getCommon got exception");
            return null;
        }
    }

    /**
     * 存键值对，可设置缓存时间，默认缓存一周
     * @param key
     * @param value
     */
    public static void setCommon(String key, Object value) {
        setCommon(key, value, Default_TIME, TimeUnit.SECONDS);
    }

    public static void setCommon(String key, Object value, Long time, TimeUnit unit) {
        if (key == null || value == null || "".equals(key)) {
            log.warn(TAG + "setCommon-params not assigned");
            return;
        }

        try {
            redisTemplate.opsForValue().set(key, value, time, unit);
        } catch (Exception e) {
            log.error(TAG + "setCommon got exception");
        }
    }

    //endregion

    //region list ops

    /**
     * 获取缓存中key对应的list
     * @param key
     * @return
     */
    public List<Object> getListAll(String key) {
        return getListRange(key, (long) 0, getListSize(key));
    }

    /**
     * 获取list的一部分
     * @param key
     * @param from included
     * @param to excluded
     * @return
     */
    public List<Object> getListRange(String key, Long from, Long to) {
        if (key == null || "".equals(key) || to <= from) {
            log.warn(TAG + "getListRange-wrong params");
            return null;
        }

        try {
            return redisTemplate.opsForList().range(key, from, to);
        } catch (Exception e) {
            log.error(TAG + "getListRange got exception");
            return null;
        }
    }

    /**
     * 获取list长度
     * @param key
     * @return
     */
    public Long getListSize(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getListSize-null params or empty");
            return null;
        }

        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(TAG + "getListSize got exception");
            return null;
        }
    }

    /**
     * 获取list对应位置对象
     * @param key
     * @param index
     * @return
     */
    public Object indexOf(String key, Long index) {
        if (key == null || "".equals(key) || index < 0) {
            log.warn(TAG + "indexOf-wrong params");
            return null;
        }

        try {
            return redisTemplate.opsForList().indexOf(key, index);
        } catch (Exception e) {
            log.error(TAG + "indexOf got exception");
            return null;
        }
    }

    /**
     * 将list存放到缓存中
     * @param key
     * @param values
     */
    public void setList(String key, List<Object> values) {
        setList(key, values, Default_TIME, TimeUnit.SECONDS);
    }

    public void setList(String key, List<Object> values, Long time, TimeUnit unit) {
        if (key == null || "".equals(key) || values == null) {
            log.warn(TAG + "setList-params wrong");
            return;
        }

        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            expire(key, time, unit);
        } catch (Exception e) {
            log.error(TAG + "setList got exception");
        }
    }

    //endregion

    //region hash ops
    //endregion

    //region set ops
    //endregion
}
