package org.jeecg.modules.agency.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgencyBalanceVO {
    //代理账号
    private String username;
    //手机号
    private String phone;
    //用户名字
    private String realname;
    //角色
    private String roleName;
    //余额
    private String balance;
}
