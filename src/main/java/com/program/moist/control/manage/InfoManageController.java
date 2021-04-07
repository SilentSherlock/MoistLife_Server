package com.program.moist.control.manage;

import com.program.moist.entity.infoEntities.Category;
import com.program.moist.service.InfoService;
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
@RestController
@Slf4j
@RequestMapping("/manage")
public class InfoManageController {

    @Resource
    private InfoService infoService;

    @RequestMapping("/addCategory")
    public Result addCategory(Category category) {
        infoService.addCategory(category);
        return Result.createBySuccess();
    }

    @RequestMapping("/updateCategory")
    public Result updateCategory(Category category) {
        infoService.updateCategory(category);
        return Result.createBySuccess();
    }
}
