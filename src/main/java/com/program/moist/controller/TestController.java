package com.program.moist.controller;

import com.program.moist.dao.infoEntities.CommentDao;
import com.program.moist.entity.infoEntities.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Date: 2021/2/26
 * Author: SilentSherlock
 * Description: Test features
 */

@Controller
public class TestController {

    @Resource
    private CommentDao commentDao;

    @RequestMapping(value = "testComment")
    @ResponseBody
    public String testComment() {
        return Arrays.toString(commentDao.getAll().toArray(new Comment[]{}));
    }
}
