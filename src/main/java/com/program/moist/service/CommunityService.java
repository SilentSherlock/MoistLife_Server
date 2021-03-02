package com.program.moist.service;

import com.program.moist.dao.infoEntities.CommentDao;
import com.program.moist.dao.infoEntities.PostDao;
import com.program.moist.dao.relations.FavPostDao;
import com.program.moist.dao.relations.ThumbUpCommentDao;
import com.program.moist.dao.relations.ThumbUpPostDao;
import com.program.moist.entity.infoEntities.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Date: 2021/3/2
 * Author: SilentSherlock
 * Description: supply community operations, post operations and comment operations
 */
@Service
@Slf4j
public class CommunityService {

    @Resource
    private PostDao postDao;
    @Resource
    private CommentDao commentDao;
    @Resource
    private FavPostDao favPostDao;
    @Resource
    private ThumbUpCommentDao thumbUpCommentDao;
    @Resource
    private ThumbUpPostDao thumbUpPostDao;

    private static final String TAG = "CommunityService-";

    public void addPost(Post post) {
        String name = "addPost-";
        log.info(TAG + name);
        int result = postDao.insert(post);
        if (result == 0) log.info("add info failed");
        else log.info("add info success");
    }

    public void deleteByMap(Map<String, Object> map) {
        String name = "deleteByMap-";
        log.info(TAG + name);
    }
}
