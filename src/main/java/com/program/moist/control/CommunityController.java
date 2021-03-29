package com.program.moist.control;

import com.program.moist.entity.infoEntities.Comment;
import com.program.moist.entity.infoEntities.Post;
import com.program.moist.entity.infoEntities.Topic;
import com.program.moist.entity.person.User;
import com.program.moist.entity.relations.ThumbUpComment;
import com.program.moist.entity.relations.ThumbUpPost;
import com.program.moist.entity.relations.TopicSub;
import com.program.moist.service.CommunityService;
import com.program.moist.service.PersonService;
import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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
    @Resource
    private PersonService personService;

    /**
     * 根据Redis中缓存的热门主题，取Post
     * @return
     */
    @RequestMapping("/browse/defaultPost")
    public Result getDefaultPosts() {
        Result result = new Result();

        List<Topic> topics;
        if (RedisUtil.hasKey(TokenUtil.TOP_TOPIC)) {
            topics = JsonUtil.string2Obj(RedisUtil.getCommon(TokenUtil.TOP_TOPIC), List.class, Topic.class);
        } else {
            topics = communityService.getTopKTopic(null);
            RedisUtil.setCommon(TokenUtil.TOP_TOPIC, JsonUtil.obj2String(topics));
        }

        if (topics == null) {
            result.setStatus(Status.WRONG_REQUEST);
            return result;
        }
        List<Post> posts = new ArrayList<>();
        for (Topic topic :
                topics) {
            posts.addAll(communityService.getPostByPage(1, 3, "topic_id", topic.getTopic_id()));
        }

        result.setStatus(Status.SUCCESS);
        result.getResultMap().put(TokenUtil.DEFAULT_POST, posts);

        return result;
    }

    @RequestMapping("/browse/getPostByUserId")
    public Result getPostByUserId(Integer userId) {
        Result result = new Result();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        List<Post> posts = communityService.getPostByMap(params);
        if (posts == null) {
            result.setStatus(Status.WRONG_REQUEST);
        } else {
            result.setStatus(Status.SUCCESS);
            result.getResultMap().put(TokenUtil.DEFAULT_POST, posts);
        }
        return result;
    }

    @RequestMapping("/browse/getCommentByPostId")
    public Result getCommentByPostId(Integer postId) {
        Result result = new Result();

        Map<String, Object> params = new HashMap<>();
        params.put("post_id", postId);
        List<Comment> comments = communityService.getCommentByMap(params);
        if (comments == null) {
            result.setStatus(Status.WRONG_REQUEST);
        } else {
            result.setStatus(Status.SUCCESS);
            result.getResultMap().put(TokenUtil.COMMENTS, comments);
        }
        return result;
    }
    /**
     * 用户登录之后的自动推荐，根据用户关注topic进行推荐
     * 如果为空，则推荐topic关注数最多的post
     * @param userId
     * @return
     */
    @RequestMapping("/getPost")
    public Result getPost(Integer userId) {
        Result result = new Result();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        List<TopicSub> topicSubs = communityService.getTopicSubByMap(params);

        List<Topic> topics;
        if (topicSubs == null || topicSubs.size() == 0) topics = communityService.getTopKTopic(null);
        else {
            topics = communityService.getTopicByTopicSubs(topicSubs);
        }

        if (topics == null || topics.size() == 0) {
            result.setStatus(Status.DEFAULT);
            result.setDescription("没有数据！！！可能这是测试呢:-)");
            return result;
        }

        List<Post> posts = new LinkedList<>();
        for (Topic topic:
             topics) {
            posts.addAll(communityService.getPostByPage(1, 3, "topic_id", topic.getTopic_id()));
        }

        result.setStatus(Status.SUCCESS);
        result.getResultMap().put(TokenUtil.DEFAULT_POST, posts);
        return result;
    }

    /**
     * 获取用户的关注者的发帖
     * @param userId
     * @return 返回post列表，如果用户没有关注，则返回一些热门用户
     */
    @RequestMapping("/getFollowPost")
    public Result getFollowPost(Integer userId) {
        Result result = new Result();

        List<User> follows = personService.getUserFollowing(userId);
        if (follows == null || follows.size() == 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_kind", ConstUtil.U_MEDIUM);
            follows = personService.getUserByMap(params);

            result.setDescription("没有关注用户呢,推荐一些:-)");
            result.getResultMap().put(TokenUtil.USERS, follows);
            return result;
        }

        List<Post> post = new LinkedList<>();
        for (User user :
                follows) {
            post.addAll(communityService.getPostByPage(1, 3, "user_id", user.getUser_id()));
        }//默认每个关注取3篇，后续关注推荐机制有待修改

        result.setStatus(Status.SUCCESS);
        result.getResultMap().put(TokenUtil.DEFAULT_POST, post);

        return result;
    }

    @RequestMapping("/addPost")
    public Result addPost(Post post) {
        Result result = new Result(Status.SUCCESS, "add post success");
        communityService.addPost(post);
        return result;
    }

    @RequestMapping("/deletePost")
    public Result deletePost(Integer postId) {
        Result result = new Result();
        Post post = communityService.getPostById(postId);
        post.setPost_state(ConstUtil.P_DELETE);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/addThumbUpPost")
    public Result addThumbUpPost(ThumbUpPost thumbUpPost) {
        Result result = new Result();
        communityService.addThumbUpPost(thumbUpPost);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/deleteThumbUpPost")
    public Result deleteThumbUpPost(ThumbUpPost thumbUpPost) {
        Result result = new Result(Status.SUCCESS);
        communityService.deleteThumbUpPost(thumbUpPost.getUser_id(), thumbUpPost.getPost_id());
        return result;
    }

    @RequestMapping("/addComment")
    public Result addComment(Comment comment) {
        Result result = new Result(Status.SUCCESS);
        communityService.addComment(comment);
        result.setDescription("add comment success");
        return result;
    }

    @RequestMapping("/deleteComment")
    public Result deleteComment(Integer comId) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("com_id", comId);
        communityService.deleteCommentByMap(params);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/addThumbUpComment")
    public Result addThumbUpComment(ThumbUpComment thumbUpComment) {
        Result result = new Result();
        communityService.addThumbUpComment(thumbUpComment);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/addTopicSub")
    public Result addTopicSub(TopicSub topicSub) {
        Result result = new Result();
        communityService.addTopicSub(topicSub);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/deleteTopicSub")
    public Result deleteTopicSub(TopicSub topicSub) {
        Result result = new Result();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", topicSub.getUser_id()); 
        params.put("topic_id", topicSub.getTopic_id());

        communityService.deleteTopicSubByMap(params);
        result.setStatus(Status.SUCCESS);
        return result;
    }
}
