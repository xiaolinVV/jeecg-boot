package org.jeecg.modules.marketing.dto;


import lombok.Data;

import java.util.Date;

/**
 * 活动记录列表查询条件
 */
@Data
public class MarketingActivityListRecordDTO {

    private String serialNumber;//活动编号

    private String activityName;//活动名称

    private String phone;//会员账号

    private String nickName;//会员昵称

    private String linkman;//联系人

    private String contactNumber;//联系电话

    private Date registrationTimeStart;//报名时间开始

    private Date registrationTimeEnd;//报名时间结束
}
