package com.program.moist.entity.person;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: admin bean
 */
@Data
public class Admin {

    private Integer admin_id;
    private String admin_name;
    private String password;
}
