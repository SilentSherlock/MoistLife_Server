package com.program.moist.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * Date: 2021/4/6
 * Author: SilentSherlock
 * Description: encode and decode valuable information
 */
@Slf4j
public class Base64Util {
    public static String encode(String origin) {
        if (origin == null) {
            log.error("origin string is null, cant encode");
            return null;
        }
        return Base64.getEncoder().encodeToString(origin.getBytes());
    }

    public static String decode(String target) {
        if (target == null) {
            log.error("target string is null, cant decode");
            return null;
        }
        return new String(Base64.getDecoder().decode(target));
    }
}
