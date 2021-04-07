package com.program.moist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.moist.dao.infoEntities.CommentDao;
import com.program.moist.dao.infoEntities.PostDao;
import com.program.moist.dao.infoEntities.TopicDao;
import com.program.moist.dao.relations.FavPostDao;
import com.program.moist.dao.relations.ThumbUpCommentDao;
import com.program.moist.dao.relations.ThumbUpPostDao;
import com.program.moist.dao.relations.TopicSubDao;
import com.program.moist.entity.infoEntities.Comment;
import com.program.moist.entity.infoEntities.Post;
import com.program.moist.entity.infoEntities.Topic;
import com.program.moist.entity.relations.FavPost;
import com.program.moist.entity.relations.ThumbUpComment;
import com.program.moist.entity.relations.ThumbUpPost;
import com.program.moist.entity.relations.TopicSub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private TopicDao topicDao;
    @Resource
    private TopicSubDao topicSubDao;
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

    //region topic crud

    /**
     *
     * @param topic
     */
    public void addTopic(Topic topic) {
        String name = "addTopic-";
        log.info(TAG + name);
        int result = topicDao.insert(topic);
        if (result == 0) log.info(TAG + name + " add info failed");
        else log.info(TAG + name + " add info success");
    }

    /**
     *
     * @param params
     */
    public void deleteTopicByMap(Map<String, Object> params) {
        String name = "deleteTopicByMap-";
        log.info(TAG + name);
        int result = topicDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + " delete failed");
        else log.info(TAG + name + " no rows effected");
    }

    /**
     *
     * @param topic
     */
    public void updateTopic(Topic topic) {
        String name = "updateTopic-";
        log.info(TAG + name);
        int result = topicDao.updateById(topic);
        if (result == 0) log.info(TAG + name + " update failed");
        else log.info(TAG + name + " no rows effected");
    }

    /**
     *
     * @return
     */
    public List<Topic> getAllTopic() {
        String name = "getAllTopic-";
        log.info(TAG + name);
        return topicDao.getAll();
    }

    /**
     *
     * @param ids
     * @return
     */
    public List<Topic> getTopicByIds(List<Integer> ids) {
        return topicDao.getByIds(ids);
    }

    public List<Topic> getTopicByTopicSubs(List<TopicSub> subs) {
        List<Integer> ids = new LinkedList<>();
        for (TopicSub sub :
                subs) {
            ids.add(sub.getTopicId());
        }
        return getTopicByIds(ids);
    }
    //endregion
    //region topicSub crud

    /**
     *
     * @param topicSub
     */
    public void addTopicSub(TopicSub topicSub) {
        String name = "addTopicSub-";
        log.info(TAG + name);
        int result = topicSubDao.insert(topicSub);
        if (result == 0) log.info(TAG + name + " inserted failed");
        else log.info(TAG + name + result + " rows inserted");
    }

    /**
     *
     * @param params
     */
    public void deleteTopicSubByMap(Map<String, Object> params) {
        String name = "deleteTopicSubByMap-";
        log.info(TAG + name);
        int result = topicSubDao.deleteByMap(params);
        if (result == 0) log.info(TAG + name + " delete failed");
        else log.info(TAG + name + result + " rows deleted");
    }

    /**
     *
     * @return
     */
    public List<TopicSub> getAllTopicSub() {
        String name = "getAllTopicSub-";
        log.info(TAG + name);
        return topicSubDao.getAll();
    }

    /**
     *
     * @param params
     * @return
     */
    public List<TopicSub> getTopicSubByMap(Map<String, Object> params) {
        String name = "getTopicSubByMap-";
        log.info(TAG + name);
        return topicSubDao.selectByMap(params);
    }

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

    /**
     *
     * @param ids
     * @return
     */
    public List<Post> getPostByIds(List<Integer> ids) {
        String name = "getPostByIds-";
        log.info(TAG + name);
        return postDao.getByIds(ids);
    }

    /**
     * 根据分页获取post
     * @param index 分页码
     * @param size 长度
     * @param name
     * @param value
     * @return
     */
    public List<Post> getPostByPage(Integer index, Integer size, String name, Object value) {
        Page<Post> page = new Page<>(index, size);
        IPage<Post> iPage = postDao.getByPage(page, name, value);
        return iPage.getRecords();
    }

    /**
     *
     * @param postId
     * @return
     */
    public Post getPostById(Integer postId) {
        return postDao.selectById(postId);
    }

    public void updatePost(Post post) {
        postDao.updateById(post);
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
            ids.add(post.getPostId());
        }

        return postDao.getByIds(ids);
    }

    /**
     * 获取关注前k的topic
     * @param k 默认为10
     * @return
     */
    public List<Topic> getTopKTopic(Integer k) {
        if (k == null) k = 10;
        List<TopicSub> list = topicSubDao.getAll();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (TopicSub topicSub : list) {
            Integer count = map.getOrDefault(topicSub.getTopicId(), 0);
            map.put(topicSub.getTopicId(), ++count);
        }

        Set<Map.Entry<Integer, Integer>> set = map.entrySet();
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(set.size(), Comparator.comparingInt(Map.Entry::getValue));
        queue.addAll(set);

        List<Topic> result = new LinkedList<>();
        k = Math.min(set.size(), k);
        for (int i = 0; i < k; i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("topic_id", Objects.requireNonNull(queue.poll()).getKey());
            result.addAll(topicDao.selectByMap(params));
        }

        return result;
    }
    //endregion
}
