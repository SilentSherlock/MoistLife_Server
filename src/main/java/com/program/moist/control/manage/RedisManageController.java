package com.program.moist.control.manage;

import com.program.moist.utils.RedisUtil;
import com.program.moist.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 2021/4/7
 * Author: SilentSherlock
 * Description: describe the class features
 */
@RequestMapping("/manage")
@RestController
@Slf4j
public class RedisManageController {

    @RequestMapping("/before/deleteAll")
    public Result deleteAll() {
        RedisUtil.deleteAll();
        return Result.createBySuccess();
    }
}
