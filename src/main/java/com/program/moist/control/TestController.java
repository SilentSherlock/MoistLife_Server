package com.program.moist.control;

import com.program.moist.dao.infoEntities.CommentDao;
import com.program.moist.entity.infoEntities.Comment;
import com.program.moist.utils.Result;
import com.program.moist.utils.Status;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Result testComment() {
        List<Comment> comments = commentDao.getAll();
        Result result = new Result(Status.SUCCESS, "测试返回的Comments");
        Map<String, Object> map = result.getResultMap();
        map.put("Comments", comments.toArray(new Comment[]{}));
        return result;
    }
}
