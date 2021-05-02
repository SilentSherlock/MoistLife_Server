package com.program.moist.entity.person;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: the bean of user
 */
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private String identifyNumber;
    private Integer userKind;
    private String userAvatar;
    private String userBackground;
}
