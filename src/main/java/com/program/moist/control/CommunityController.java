package com.program.moist.control;

import com.program.moist.service.CommunityService;
import com.program.moist.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date: 2021/3/12
 * Author: SilentSherlock
 * Description: accept user's or admin's request of post or comment, deal with the request,
 * operate the DB or return data with JSON type
 */
@RestController
@Slf4j
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @RequestMapping("/browse/posts")
    public Result getDefaultPosts() {
        Result result = new Result();
        return result;
    }

}
