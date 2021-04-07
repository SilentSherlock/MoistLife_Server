package com.program.moist.entity.person;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: admin bean
 */
@Data
public class Admin {

    private Integer adminId;
    private String adminName;
    private String password;

    public Admin(Integer i, String valueOf, String valueOf1) {
        this.adminId = i;
        this.adminName = valueOf;
        this.password = valueOf1;
    }

    public Admin() {

    }
}
