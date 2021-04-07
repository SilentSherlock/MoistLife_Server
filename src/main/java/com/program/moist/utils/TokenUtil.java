package com.program.moist.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
