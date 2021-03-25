package com.program.moist.control;

import com.program.moist.entity.infoEntities.Post;
import com.program.moist.entity.infoEntities.Topic;
import com.program.moist.entity.relations.TopicSub;
import com.program.moist.service.CommunityService;
import com.program.moist.service.PersonService;
import com.program.moist.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        List<Integer> ids = new LinkedList<>();
        for (Topic topic :
                topics) {
            ids.add(topic.getTopic_id());
        }
        List<Post> posts = communityService.getPostByIds(ids);

        result.setStatus(Status.SUCCESS);
        result.getResultMap().put("default_post", posts);

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
        result.getResultMap().put("default_post", posts);
        return result;
    }
}
