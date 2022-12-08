package org.jeecg.modules.member.dto;


import lombok.Data;

import java.util.Date;

@Data
public class MemberDistributionLevelDTO {

    private String phone;

    private String nickName;

    private String levelName;

    private Date updateTimeStart;

    private Date updateTimeEnd;
}
