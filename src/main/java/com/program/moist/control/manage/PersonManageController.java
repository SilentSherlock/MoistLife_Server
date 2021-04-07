package com.program.moist.control.manage;

import com.program.moist.entity.person.Admin;
import com.program.moist.service.PersonService;
import com.program.moist.utils.RedisUtil;
import com.program.moist.utils.Result;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Date: 2021/4/7
 * Author: SilentSherlock
 * Description: describe the class features
 */
@RequestMapping("/manage")
@RestController
@Slf4j
public class PersonManageController {

    @Resource
    private PersonService personService;

    @RequestMapping("/before/login")
    public Result login(String adminName, String password) {
        Admin admin = personService.adminValidate(adminName, password);
        if (admin == null) return Result.createByFalse("wrong admin account");
        Result result = Result.createBySuccess();

        String admin_token = TokenUtil.getUUID();
        result.getResultMap().put(TokenUtil.LOGIN_TOKEN, admin_token);
        //result.getResultMap().put(TokenUtil.ADMIN, admin);

        RedisUtil.putMapValue(TokenUtil.LOGIN_TOKEN, admin_token, admin);
        return result;
    }

    @RequestMapping("/addAdmin")
    public Result addAdmin(Admin admin) {
        personService.addAdmin(admin);
        return Result.createBySuccess();
    }
}
