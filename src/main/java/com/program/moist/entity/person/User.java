package com.program.moist.entity.person;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: the bean of user
 */
@Data
public class User {

    private Integer user_id;
    private String user_name;
    private String phone_number;
    private String email;
    private String password;
    private String identify_number;
    private Integer user_kind;
    private String user_avatar;
}
