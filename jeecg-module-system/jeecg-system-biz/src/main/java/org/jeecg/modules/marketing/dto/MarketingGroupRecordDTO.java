package org.jeecg.modules.marketing.dto;

import lombok.Data;

@Data
public class MarketingGroupRecordDTO {

    private String groupNo;//团号
    private String groupRecordNo;//参团编号
    private String tuxedoTimeStart;//参团时间开始
    private String tuxedoTimeEnd;//参团时间结束
    private String goodNo;//商品编号
    private String goodName;//商品名称
    private String phone;//会员手机号
    private String ptStatus;//拼团状态；0：进行中；1：已成功；2：已失败
    private String identityStatus;//拼团身份；0：参与人；1：发起人
    private String buyStartTimeStart;//购买开始时间开始
    private String buyStartTimeEnd;//购买开始时间结束
    private String buyEndTimeStart;//购买结束时间开始
    private String buyEndTimeEnd;//购买结束时间结束
    private String status;//拼团记录状态

}
