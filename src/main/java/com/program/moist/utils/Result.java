package com.program.moist.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2021/3/12
 * Author: SilentSherlock
 * Description: enclose the response of Controllers
 */
@Data
public class Result {

    private Status status;
    private String description;
    private Map<String, Object> resultMap;

    Result(Status status, String msg, Map<String, Object> map) {
        this.status = status;
        this.description = msg;
        this.resultMap = map;
    }

    Result() {
        this(Status.DEFAULT, "", new HashMap<>());
    }

    Result(Status status) {
        this(status, "", new HashMap<>());
    }

    Result(Status status, String msg) {
        this(status, msg, new HashMap<>());
    }
}
