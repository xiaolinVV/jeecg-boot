package org.jeecg.modules.marketing.dto;


import lombok.Data;

import java.util.Date;

@Data
public class MarketingBusinessGiftTeamRecordDTO {

    private String serialNumber;//编号

    private String mid;//会员编号

    private String phone;//手机号

    private Date createTimeStart;

    private Date createTimeEnd;

    private String performance;

    private String status;
}
