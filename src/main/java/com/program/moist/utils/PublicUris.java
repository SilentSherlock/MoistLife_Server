package com.program.moist.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2021/3/21
 * Author: SilentSherlock
 * Description: URI that not need login
 */
public class PublicUris {
    public static List<String> uris = new LinkedList<>();

    static {
        uris.add("/info/browse/");
    }
}
