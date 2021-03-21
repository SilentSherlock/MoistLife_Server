package com.program.moist.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Date: 2021/3/15
 * Author: SilentSherlock
 * Description: parse Object to Json String and Json String to Object
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);//序列化所有字段
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("");
            }
        });//字段值为空时，用""代替
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//忽略空bean转json的错误
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//不将时间转换为时间戳
        mapper.setDateFormat(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"));//设置时间样式
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略JSON字符串中有bean没有的属性的错误
    }

    /**
     * 对象转换为JSON字符串
     * @param object 可以是单个对象也可以是Collection
     * @return
     */
    public static String obj2String(Object object) {
        if (null == object) return null;

        try {
            return object instanceof String ? (String) object : mapper.writeValueAsString(object);
        }catch (Exception e) {
            log.error("JsonUtil parse obj as string wrong", e);
            return null;
        }
    }

    /**
     * 根据给出的对象类型将JSON字符串转换为单个对象
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> type) {
        if (str == null || "".equals(str) || type == null) return null;

        try {
            return String.class.equals(type) ? (T) str : mapper.readValue(str, type);
        } catch (Exception e) {
            log.error("JsonUtil parse string as object wrong");
            return null;
        }
    }

    /**
     * 将JSON字符串转换为集合类的对象
     * example string2Obj(str, List.class, Set<Integer>.class, Integer.class)
     * @param str
     * @param collectionClass 集合类型
     * @param elementClass 子元素类型，可嵌套
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClass) {
        if (str == null || "".equals(str) || collectionClass == null || elementClass.length == 0) {
            log.warn("JsonUtil string2Obj wrong params");
            return null;
        }

        try {
            return mapper.readValue(str,
                    mapper.getTypeFactory().constructParametricType(collectionClass, elementClass));
        } catch (Exception e) {
            log.error("JsonUtil parse string as object wrong", e);
            return null;
        }
    }


}
