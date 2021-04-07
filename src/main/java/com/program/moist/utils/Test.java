package com.program.moist.utils;

import com.program.moist.entity.person.Admin;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.expression.Lists;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2021/3/18
 * Author: SilentSherlock
 * Description: describe the class features
 */
public class Test {

    public static void main(String[] args) throws IOException {

        //Json test
        Admin admin = new Admin();
        admin.setAdmin_id(3232);
        admin.setAdmin_name("fuck");

        List<Admin> adminList = new LinkedList<>();
        adminList.add(admin);
        for (int i = 0;i < 2;i++) {
            adminList.add(new Admin(i, String.valueOf(i), String.valueOf(i)));
        }

        System.out.println("Admin:" + JsonUtil.obj2String(admin));
        System.out.println("AdminList:" + JsonUtil.obj2String(adminList));
        System.out.println("Admin" + JsonUtil.string2Obj(JsonUtil.obj2String(admin), Admin.class));
        System.out.println("AdminList:" + JsonUtil.string2Obj(JsonUtil.obj2String(adminList), List.class, Admin.class));

        for (int i = 0; i < 5; i++) {
            System.out.println(TokenUtil.getUUID());
        }

        File file = new File("D:/files/tips/pom.xml");
        List<File> files = new ArrayList<>();
        files.add(file);
        FTPUtil.upload("", files);
    }
}
