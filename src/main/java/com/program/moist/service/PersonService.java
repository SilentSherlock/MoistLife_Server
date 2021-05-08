package com.program.moist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.moist.dao.person.AdminDao;
import com.program.moist.dao.person.UserDao;
import com.program.moist.dao.relations.FollowDao;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.person.Admin;
import com.program.moist.entity.person.User;
import com.program.moist.entity.relations.Follow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Date: 2021/3/2
 * Author: SilentSherlock
 * Description: supply person operations and features
 */
@Service
@Slf4j
public class PersonService {

    //region class params
    @Resource
    private AdminDao adminDao;
    @Resource
    private UserDao userDao;
    @Resource
    private FollowDao followDao;

    public static final String TAG = "PersonService-";
    //endregion

    //region follow crud

    /**
     *
     * @param follow
     */
    public void addFollow(Follow follow) {
        int result = followDao.insert(follow);
        if (result == 0) log.info("add new follow failed");
        else log.info("add follow success");
    }

    /**
     *
     * @param follow
     */
    public void deleteFollow(Follow follow) {
        Map<String, Object> params = new HashMap<>();
        params.put("from_user_id", follow.getFromUserId());
        params.put("to_user_id", follow.getToUserId());
        int result = followDao.deleteByMap(params);
        if (result == 0) log.info("delete no effect");
        else log.info("delete " + result + " follows");
    }

    /**
     * 获取该用户的关注
     * @param fromId
     * @return
     */
    public List<User> getUserFollowing(Integer fromId) {
        if (fromId == null) {
            log.info("get user following - params is null");
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("from_user_id", fromId);
        List<Follow> follows = followDao.selectByMap(params);
        if (follows == null || follows.size() == 0) return new LinkedList<>();

        List<Integer> list = new LinkedList<>();
        for (Follow follow :
                follows) {
            list.add(follow.getToUserId());
        }
        return userDao.getByIds(list);
    }

    /**
     * 获取关注该用户的人
     * @param toId
     * @return
     */
    public List<User> getUserFollowed(Integer toId) {
        if (toId == null) {
            log.info("get user followed - params is null");
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("to_user_id", toId);
        List<Follow> followers = followDao.selectByMap(params);
        if (followers == null || followers.size() == 0) return new LinkedList<>();

        List<Integer> ids = new ArrayList<>();
        for (Follow f :
                followers) {
            ids.add(f.getFromUserId());
        }
        return userDao.getByIds(ids);
    }

    /**
     * 分页查询用户
     * @param index 查询页码
     * @param size 一页用户数量
     * @param name 查询列名
     * @param value 查询列值
     * @return
     */
    public List<User> getUserByPage(Integer index, Integer size, String name, String value) {
        Page<User> page = new Page<>(index, size);
        String method = "getUserByPage-";
        log.info(TAG + method);
        IPage<User> result;
        if (name.endsWith("id") || name.endsWith("kind")) {
            result = userDao.getByPage(page, name, Integer.parseInt(value));
        } else result = userDao.getByPage(page, name, value);
        if (result == null) log.warn(TAG + method + "get nothing");
        return result.getRecords();
    }
    //endregion

    //region admin crud

    /**
     * @Description add admin
     * @param admin the system admin
     */
    public void addAdmin(Admin admin) {
        String name = "addAdmin-";
        log.info(TAG + name);

        int result = adminDao.insert(admin);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + "rows insert");
    }

    /**
     * @param adminId
     */
    public void deleteAdmin(Integer adminId) {
        String name = "deleteAdmin-";
        log.info(TAG + name);

        int result = adminDao.deleteById(adminId);
        if (result == 0) log.info(TAG + name + "no rows effected");
        else log.info(TAG + name + result + " rows effected");
    }

    /**
     * @return List
     */
    public List<Admin> getAllAdmin() {
        String name = "getAllAdmin-";
        log.info(TAG + name);

        return adminDao.getAllUser();
    }

    /**
     *
     * @param adminName
     * @param password
     * @return Admin
     */
    public Admin adminValidate(String adminName, String password) {
        String name = "adminValidate-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("admin_name", adminName);
        params.put("password", password);
        List<Admin> result = adminDao.selectByMap(params);

        if (null == result || result.size() != 1) {
            log.info(TAG + name + "validate failed");
            return null;
        }

        return result.get(0);
    }
    //endregion

    //region user crud

    /**
     *
     * @param name
     * @param value
     * @param userId
     */
    public void updateUserColumnById(String name, Object value, Integer userId) {
        int result = userDao.updateColumnById(name, value, userId);
        if (result == 0) log.warn("update user column failed or no use");
        else log.info("update column success, " + result + " rows effected");
    }
    /**
     * @param user
     */
    public void addUser(User user) {
        String name = "addUser-";
        log.info(TAG + name);

        int result = userDao.insert(user);
        if (result == 0) log.info(TAG + name + "failed");
        else log.info(TAG + name + result + " rows insert");
    }

    /**
     *
     * @param user
     */
    public void updateUser(User user) {
        String name = "updateUser-";
        log.info(TAG + name);

        int result = userDao.updateById(user);
        if (result == 0) log.info(TAG + name + "no rows effected");
        else log.info(TAG + name + result + " rows effected");
    }


    /**
     *
     * @param params
     * @return
     */
    public List<User> getUserByMap(Map<String, Object> params) {
        log.info("get user by map");
        return userDao.selectByMap(params);
    }

    /**
     * 取简化版的user信息
     * @param userId
     * @return
     */
    public User getSimpleUser(Integer userId) {
        return userDao.getUser(userId);
    }
    //endregion

    //region mixed method

    /**
     * Validate user account by phone number
     * @param phoneNumber
     * @param password
     */
    public User userValidateByPhone(String phoneNumber, String password) {
        String name = "userValidateByPhone-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("phone_number", phoneNumber);
        params.put("password", password);
        List<User> result = userDao.selectByMap(params);

        if (result == null || result.size() != 1) {
            log.info(TAG + name + "user validate failed");
            return null;
        }

        return result.get(0);
    }

    /**
     * Validate user account by email
     * @param email
     * @param password
     * @return User
     */
    public User userValidateByEmail(String email, String password) {
        String name = "userValidateByEmail-";
        log.info(TAG + name);

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        List<User> result = userDao.selectByMap(params);

        if (result == null || result.size() != 1) {
            log.info(TAG + name + "user validate failed");
            return null;
        }

        return result.get(0);
    }

    /**
     * 校验用户信息是否重复
     * @param msg
     * @param type 0-邮箱 1-手机号 2-身份证号
     * @return
     */
    public boolean checkMsg(String msg, Integer type) {
        Integer res = null;
        switch (type) {
            case 0:
                res = userDao.checkByEmail(msg);
                log.info("checking email " + res);
                break;
            case 1:
                res = userDao.checkByPhone(msg);
                log.info("checking phone " + res);
                break;
            case 2:
                res = userDao.checkByIN(msg);
                log.info("checking identify number " + res);
                break;
            default:
                log.info("checkMsg get wrong type");
        }
        return res == null || res == 0;
    }
    //endregion
}
