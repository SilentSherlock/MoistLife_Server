package com.program.moist.others.cfgs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Date: 2021/3/15
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Configuration
public class SpringConfig {

    /**
     * Jackson config
     * @param builder
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);//只序列化非空字段
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//忽略空bean转json的错误
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//不将时间转换为时间戳
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));//设置时间样式
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略JSON字符串中有bean没有的属性的错误

        return mapper;
    }

    /**
     * Multipart config
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(13));
        return factory.createMultipartConfig();
    }
}
