package com.program.moist.entity;

import lombok.Data;

/**
 * Date: 2021/5/8
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Data
public class StsToken {

    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;

    public StsToken(String key, String secret, String token) {
        this.accessKeyId = key;
        this.accessKeySecret = secret;
        this.securityToken = token;
    }

    public StsToken(){

    }
}
