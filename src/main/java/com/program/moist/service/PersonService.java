package com.program.moist.service;

import com.program.moist.dao.person.AdminDao;
import com.program.moist.dao.person.UserDao;
import com.program.moist.entity.person.Admin;
import com.program.moist.entity.person.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final String TAG = "PersonService-";
    //endregion

    //region admin crud

    /**
     * @Description add admin
     * @param admin
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
     * @return
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
    //endregion

}
