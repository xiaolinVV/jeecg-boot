package org.jeecg.modules.jwt.model;

import lombok.Data;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * @author ScienJus
 * @date 2015/7/31.
 */
@Data
public class TokenModel {

    //用户id
    private String memberId;

    private String appid;

    //随机生成的uuid
    private String token;

    public TokenModel(String memberId, String token, String appid) {
        this.memberId = memberId;
        this.token = token;
        this.appid=appid;
    }
}
