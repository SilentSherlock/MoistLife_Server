package com.program.moist.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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


}