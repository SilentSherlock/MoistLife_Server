package com.program.moist.others.cfgs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

/**
 * Date: 2021/3/15
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);//只序列化非空字段
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//忽略空bean转json的错误
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//不将时间转换为时间戳
        mapper.setDateFormat(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"));//设置时间样式
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略JSON字符串中有bean没有的属性的错误

        return mapper;
    }
}
