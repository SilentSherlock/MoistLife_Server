package com.program.moist.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/3/16
 * Author: SilentSherlock
 * Description: operate redis
 */
@Slf4j
@Component
public class RedisUtil {

    /**
     * 将springboot自动装配的bean注入静态变量中
     * 项目启动后静态变量才会被赋值
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static RedisUtil redisUtil;
    private static final String TAG = "RedisUtil-";
    private static final Long Default_TIME = (long) 60*60*24*7;

    @PostConstruct
    private void init() {
        redisUtil = this;
    }

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
            redisUtil.redisTemplate.expire(key, time, timeUnit);
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
            time = redisUtil.redisTemplate.getExpire(key, timeUnit);
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
            return redisUtil.redisTemplate.hasKey(key);
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
                Boolean res = redisUtil.redisTemplate.delete(key[0]);
                return res != null && res ? (long) 1 : (long) 0;
            }else {
                return redisUtil.redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
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
    public static String getCommon(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getCommon-key is null or empty");
            return null;
        }

        try {
            return (String) redisUtil.redisTemplate.opsForValue().get(key);
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
    public static void setCommon(String key, String value) {
        setCommon(key, value, Default_TIME, TimeUnit.SECONDS);
    }

    public static void setCommon(String key, String value, Long time, TimeUnit unit) {
        if (key == null || value == null || "".equals(key)) {
            log.warn(TAG + "setCommon-params not assigned");
            return;
        }

        try {
            redisUtil.redisTemplate.opsForValue().set(key, value, time, unit);
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
    public static List<Object> getListAll(String key) {
        return getListRange(key, (long) 0, getListSize(key));
    }

    /**
     * 获取list的一部分
     * @param key
     * @param from included
     * @param to excluded
     * @return
     */
    public static List<Object> getListRange(String key, Long from, Long to) {
        if (key == null || "".equals(key) || to <= from) {
            log.warn(TAG + "getListRange-wrong params");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForList().range(key, from, to);
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
    public static Long getListSize(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getListSize-null params or empty");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForList().size(key);
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
    public static Object indexOf(String key, Long index) {
        if (key == null || "".equals(key) || index < 0) {
            log.warn(TAG + "indexOf-wrong params");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForList().indexOf(key, index);
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
    public static void setList(String key, List<Object> values) {
        setList(key, values, Default_TIME, TimeUnit.SECONDS);
    }

    public static void setList(String key, List<Object> values, Long time, TimeUnit unit) {
        if (key == null || "".equals(key) || values == null) {
            log.warn(TAG + "setList-params wrong");
            return;
        }

        try {
            redisUtil.redisTemplate.opsForList().rightPushAll(key, values);
            expire(key, time, unit);
        } catch (Exception e) {
            log.error(TAG + "setList got exception");
        }
    }

    //endregion

    //region hash ops

    /**
     * 获得hash表中所有的键
     * @param key
     * @return
     */
    public static Set<Object> getMapKeys(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getMapKeys-key is null or empty");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            log.error(TAG + "getMapKeys got exception");
            return null;
        }
    }

    /**
     * 获得hash表中所有键值对
     * @param key
     * @return
     */
    public static Map<Object, Object> getMapAll(String key) {
        if (key == null || "".equals(key)) {
            log.warn(TAG + "getMapAll-key is null or empty");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error(TAG + "getMapAll got exception");
            return null;
        }
    }

    /**
     * 根据键获取表中对象
     * @param key
     * @param hashKey 确定为String类型
     * @return
     */
    public static Object getMapValue(String key, String hashKey) {
        if (key == null || hashKey == null || "".equals(key) || "".equals(hashKey)) {
            log.warn(TAG + "getMapValue-wrong parameter");
            return null;
        }

        try {
            return redisUtil.redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error(TAG + "getMapAll got exception");
            return null;
        }
    }

    /**
     * 删除一个或多个键值对
     * @param key
     * @param hashKeys
     */
    public static void deleteMap(String key, String... hashKeys) {
        if (key == null || "".equals(key) || hashKeys.length == 0) {
            log.warn(TAG + "deleteMap-keys is empty");
            return;
        }

        try {
            redisUtil.redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            log.error(TAG + "deleteMap get exception");
        }
    }

    /**
     * 存入键值对
     * @param key
     * @param hashKey
     * @param hashValue
     */
    public static void putMapValue(String key, String hashKey, Object hashValue) {
        if (key == null || "".equals(key) || hashKey == null || "".equals(hashKey) || hashValue == null) {
            log.warn(TAG + "putMapValue wrong parameters");
            return;
        }

        try {
            redisUtil.redisTemplate.opsForHash().put(key, hashKey, hashValue);
        } catch (Exception e) {
            log.error(TAG + "putMapValue get Exception");
        }
    }

    /**
     * 判断键值对是否存在
     * @param key
     * @param hashKey
     * @return
     */
    public static Boolean hasKey(String key, String hashKey) {
        if (key == null || hashKey == null || "".equals(key) || "".equals(hashKey)) {
            log.warn(TAG + "hasKey-hash parameter empty");
            return false;
        }

        try {
            return redisUtil.redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (NullPointerException e) {
            log.error(TAG + "hasKey-hash got exception");
            return false;
        }
    }
    //endregion

    //region set ops
    //endregion

}
