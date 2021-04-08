package com.program.moist.entity.person;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: admin bean
 */
@Data
public class Admin {

    @TableId(type = IdType.AUTO)
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
