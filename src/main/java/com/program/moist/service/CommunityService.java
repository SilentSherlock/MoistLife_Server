package com.program.moist.service;

import com.program.moist.dao.infoEntities.CommentDao;
import com.program.moist.dao.infoEntities.PostDao;
import com.program.moist.dao.relations.FavPostDao;
import com.program.moist.dao.relations.ThumbUpCommentDao;
import com.program.moist.dao.relations.ThumbUpPostDao;
import com.program.moist.entity.infoEntities.Comment;
import com.program.moist.entity.infoEntities.Post;
import com.program.moist.entity.relations.FavPost;
import com.program.moist.entity.relations.ThumbUpComment;
import com.program.moist.entity.relations.ThumbUpPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2021/3/2
 * Author: SilentSherlock
 * Description: supply community operations, post operations and comment operations
 */
@Service
@Slf4j
public class CommunityService {

    //region class params
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
    //endregion

    //region post crud
    /**
     * add a post
     * */
    public void addPost(Post post) {
        String name = "addPost-";
        log.info(TAG + name);
        int result = postDao.insert(post);
        if (result == 0) log.info("add info failed");
        else log.info("add info success");
    }

    /**
     * delete a post by params
     * */
    public void deletePostByMap(Map<String, Object> map) {
        String name = "deletePostByMap-";
        log.info(TAG + name);
        int result = postDao.deleteByMap(map);
        if (result == 0) log.info("no row effected");
    }

    /**
     * get posts by params
     * */
    public List<Post> getPostByMap(Map<String, Object> params) {
        String name = "getPostByMap-";
        log.info(TAG + name);
        return postDao.selectByMap(params);
    }
    //endregion

    //region comment curd

    /**
     * @Description add a comment
     * @param comment
     */
    public void addComment(Comment comment) {
        String name = "addComment-";
        log.info(TAG + name);
        int result = commentDao.insert(comment);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }

    /**
     * @Description delete comment by map, actually delete by user's only identify property
     * @param params
     */
    public void deleteCommentByMap(Map<String, Object> params) {
        String name = "deleteCommentByMap-";
        log.info(TAG + name);
        int result = commentDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " row effected");
    }

    /**
     * @Description get user's comment
     * @param params
     * @return
     */
    public List<Comment> getCommentByMap(Map<String, Object> params) {
        String name = "getCommentByMap-";
        log.info(TAG + name);

        return commentDao.selectByMap(params);
    }

    //endregion

    //region FavPost crud

    /**
     * @Description add user's favPost
     * @param favPost
     */
    public void addFavPost(FavPost favPost) {
        String name = "addFavPost-";
        log.info(TAG + name);

        int result = favPostDao.insert(favPost);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }

    /**
     * @Description delete user's favorite post
     * @param postId
     * @param userId
     */
    public void deleteFavPost(Integer postId, Integer userId) {
        String name = "deleteFavPostByMap-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("post_id", postId);
        params.put("user_id", userId);
        int result = favPostDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " row deleted");
    }

    //endregion

    //region thumbUpComment crud

    /**
     * @Description add user thumb up comment
     * @param thumbUpComment
     */
    public void addThumbUpComment(ThumbUpComment thumbUpComment) {
        String name = "addThumbUpComment-";
        log.info(TAG + name);
        int result = thumbUpCommentDao.insert(thumbUpComment);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }


    /**
     * @Description user cancel thumb up, delete user's thumb up comment
     * @param comId
     * @param userId
     */
    public void deleteThumbUpComment(Integer comId, Integer userId) {
        String name = "deleteThumbUpComment-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("com_id", comId);
        params.put("user_id", userId);
        int result = thumbUpCommentDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows deleted");
    }
    //endregion

    //region thumbUpPost crud

    /**
     * @Description add user's thumb up post
     * @param thumbUpPost
     */
    public void addThumbUpPost(ThumbUpPost thumbUpPost) {
        String name = "addThumbUpPost-";
        log.info(TAG + name);

        int result = thumbUpPostDao.insert(thumbUpPost);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }

    /**
     * @Description user canceled thumb up, delete user's thumb up post
     * @param userId
     * @param postId
     */
    public void deleteThumbUpPost(Integer userId, Integer postId) {
        String name = "deleteThumbUpPost-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("post_id", postId);
        int result = thumbUpPostDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + "no rows effected");
        else log.info(TAG + name + " row deleted");
    }

    //endregion

    //region mix methods

    /**
     * @Description get user's fav posts
     * @param userId
     * @return
     */
    public List<Post> getUserFavPost(Integer userId) {
        String name = "getUserFavPost-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        List<FavPost> favPosts = favPostDao.selectByMap(params);

        List<Integer> ids = new LinkedList<>();
        for (FavPost post :
                favPosts) {
            ids.add(post.getPost_id());
        }

        return postDao.getByIds(ids);
    }


    //endregion
}
