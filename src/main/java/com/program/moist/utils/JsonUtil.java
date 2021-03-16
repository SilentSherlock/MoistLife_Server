package com.program.moist.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Date: 2021/3/15
 * Author: SilentSherlock
 * Description: parse Object to Json String and Json String to Object
 */
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
        mapper.setDateFormat(new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"));//设置时间样式
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略JSON字符串中有bean没有的属性的错误
    }

    public static String obj2String(Object object) throws JsonProcessingException {
        if (null == object) return null;

        return object instanceof String ? (String) object : mapper.writeValueAsString(object);
    }

    public static <T> T string2Obj(String str, Class<T> type) throws JsonProcessingException {
        if (str == null || "".equals(str) || type == null) return null;

        return String.class.equals(type) ? (T) str : mapper.readValue(str, type);
    }
}
