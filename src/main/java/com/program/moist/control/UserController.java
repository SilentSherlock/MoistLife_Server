package com.program.moist.control;

import com.program.moist.entity.person.User;
import com.program.moist.entity.relations.Follow;
import com.program.moist.service.FileService;
import com.program.moist.service.PersonService;
import com.program.moist.utils.RedisUtil;
import com.program.moist.utils.Result;
import com.program.moist.utils.Status;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Date: 2021/3/12
 * Author: SilentSherlock
 * Description: accept user's or admin's request, deal with the request,
 * operate the DB or return data with JSON type
 */
@RequestMapping("/user")
@Slf4j
@RestController
public class UserController {

    @Resource
    private PersonService personService;
    @Resource
    private FileService fileService;
    /**
     * 用户登录
     * @param account 可以是邮箱也可以是手机号
     * @param password
     * @param type 标识账户是邮箱还是手机号，0-邮箱，1-手机号
     * @return
     */
    @RequestMapping("/before/login")
    public Result userLogin(String account, String password, Integer type) {
        Result result = new Result();
        User user;
        switch (type) {
            case 0:
                user = personService.userValidateByEmail(account, password);
                break;
            case 1:
                user = personService.userValidateByPhone(account, password);
                break;
            default:
                result.setStatus(Status.WRONG_REQUEST);
                result.setDescription("请求类型错误");
                return result;
        }

        if (user == null) {
            result.setStatus(Status.NOT_FOUND);
            result.setDescription("用户不存在");
            return result;
        }

        String login_token = TokenUtil.getUUID();
        RedisUtil.putMapValue(TokenUtil.LOGIN_TOKEN, login_token, user);
        result.setStatus(Status.SUCCESS);
        result.setDescription("登录成功");
        result.getResultMap().put(TokenUtil.LOGIN_TOKEN, login_token);
        //result.getResultMap().put(TokenUtil.USER, user);

        return result;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/before/register")
    public Result userRegister(User user) {
        Result result = new Result();

        personService.addUser(user);
        String login_token = TokenUtil.getUUID();
        RedisUtil.putMapValue(TokenUtil.LOGIN_TOKEN, login_token, user);
        result.setDescription("注册成功");
        result.getResultMap().put(TokenUtil.LOGIN_TOKEN, login_token);
        //result.getResultMap().put(TokenUtil.USER, user);

        return result;
    }

    /**
     * 检查标志性信息是否重复
     * @param msg
     * @param type 0-邮箱 1-手机号 2-身份证号
     * @return
     */
    @RequestMapping("/before/check")
    public Result checkMsg(String msg, Integer type) {
        Result result = new Result();
        if (personService.checkMsg(msg, type)) {
            result.setStatus(Status.SUCCESS);
            result.setDescription("信息未重复");
        } else {
            result.setStatus(Status.FALSE);
            result.setDescription("信息重复");
        }
        return result;
    }

    /**
     * 根据用户id获取用户简略信息
     * @param userId
     * @return
     */
    @RequestMapping("/before/getUserById")
    public Result getUserById(Integer userId) {
        Result result = new Result();
        User user = personService.getSimpleUser(userId);
        if (user == null) {
            result.setStatus(Status.FALSE);
            result.setDescription("用户id错误");
        } else {
            result.setStatus(Status.SUCCESS);
            result.getResultMap().put(TokenUtil.USER, user);
        }

        return result;
    }

    @RequestMapping("/addFollow")
    public Result addFollow(Follow follow) {
        Result result = new Result();
        personService.addFollow(follow);
        result.setStatus(Status.SUCCESS);
        return result;
    }

    @RequestMapping("/deleteFollow")
    public Result deleteFollow(Integer fromId) {
        Result result = new Result();
        personService.deleteFollow(fromId);
        result.setStatus(Status.SUCCESS);
        return result;

    }

    /**
     * 获得关注用户的人
     * @param toId
     * @return
     */
    @RequestMapping("/getFollowers")
    public Result getFollowers(Integer toId) {
        Result result = new Result();
        List<User> users = personService.getUserFollowed(toId);
        if (users == null || users.size() == 0) {
            result.setStatus(Status.DEFAULT);
            result.setDescription("还没有关注者");
        } else {
            result.setStatus(Status.SUCCESS);
            result.getResultMap().put(TokenUtil.USERS, users);
        }
        return result;
    }

    /**
     * 获得用户的关注
     * @param fromId
     * @return
     */
    @RequestMapping("/getFollowing")
    public Result getFollowing(Integer fromId) {
        Result result = new Result();
        List<User> users = personService.getUserFollowing(fromId);
        if (users == null || users.size() == 0) {
            result.setStatus(Status.DEFAULT);
            result.setDescription("没有关注的人");
        } else {
            result.setStatus(Status.SUCCESS);
            result.getResultMap().put(TokenUtil.USERS, users);
        }

        return result;
    }

    @RequestMapping("/addAvatar")
    public Result addAvatar(@RequestParam("avatar") MultipartFile file, Integer userId) {
        Result result = new Result();
        String path = userId + "/avatar/";
        String filePath = fileService.upload(file, path);
        result.setStatus(Status.SUCCESS);
        result.getResultMap().put(TokenUtil.PATH, filePath);

        return result;
    }

    @RequestMapping("/getUserInfo")
    public Result getUserInfo(String userToken) {
        User user = (User) RedisUtil.getMapValue(TokenUtil.LOGIN_TOKEN, userToken);
        Result result = Result.createBySuccess();
        result.getResultMap().put(TokenUtil.USER, user);

        return result;
    }
}
