package com.program.moist.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * Date: 2021/3/17
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Slf4j
@Data
public class TokenUtil {

    public static final String LOGIN_TOKEN = "login_token";
    public static final String DEFAULT_INFO = "default_info";
    public static final String INFOS = "INFOS";
    public static final String INFO = "INFO";
    public static final String CATEGORY = "category";
    public static final Long DEFAULT_TIME = (long) 60*60*24*7;
    public static final String AREA = "area";
    public static final String TOP_TOPIC = "top_topic";
    public static final String DEFAULT_POST = "default_post";
    public static final String USERS = "users";
    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String PATHS = "paths";
    public static final String PATH = "path";
    public static final String COMMENTS = "comments";
    public static final String VALIDATE_CODE = "validate_code";
    public static final String START_TIME = "start_time";
    public static final String STS_TOKEN = "sts_token";
    public static String SEARCH_DIR = "";
    public static final String DIVIDE = "Ψ";
    public static final String POSTS = "posts";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ONGOING = "ongoing";
    public static final String FINISH = "finish";

    static {
        try {
            SEARCH_DIR = ResourceUtils.getURL("classpath:").getPath() + "static/lucene/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
