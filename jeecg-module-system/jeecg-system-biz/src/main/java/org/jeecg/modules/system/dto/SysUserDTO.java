package org.jeecg.modules.system.dto;

import lombok.Data;

@Data
public class SysUserDTO {
    private String Id;
    private String userName;
    private String realname;
    private String phone;
    private String passWord;

    private String newPassword;
    /**
     * 确认密码
     */
    private String affirmPassword;

    private String sbCode;
}
