package com.program.moist.control.manage;

import com.program.moist.entity.infoEntities.Topic;
import com.program.moist.service.CommunityService;
import com.program.moist.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date: 2021/4/7
 * Author: SilentSherlock
 * Description: describe the class features
 */
@RequestMapping("/manage")
@RestController
@Slf4j
public class CommunityManageController {

    @Resource
    private CommunityService communityService;

    @RequestMapping("/addTopic")
    public Result addTopic(Topic topic) {
        communityService.addTopic(topic);
        return Result.createBySuccess();
    }

    @RequestMapping("/updateTopic")
    public Result updateTopic(Topic topic) {
        communityService.updateTopic(topic);
        return Result.createBySuccess();
    }
}
