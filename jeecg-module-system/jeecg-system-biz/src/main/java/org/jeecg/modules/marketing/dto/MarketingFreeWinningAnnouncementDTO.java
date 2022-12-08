package org.jeecg.modules.marketing.dto;


import lombok.Data;

/**
 * 中奖公告列表查询参数实体
 * 张靠勤    2021-3-19
 */
@Data
public class MarketingFreeWinningAnnouncementDTO {
    private String serialNumber;//场次编号
    private String startTimeStart;//开始时间开始
    private String startTimeEnd;//开始时间结束
    private String endTimeStart;//结束时间开始
    private String endTimeEnd;//结束时间结束
    private String freeDayStart;//免单时间开始
    private String freeDayEnd;//免单时间结束
    private String status;//发布状态
    private String releaseTimeStart;//发布时间开始
    private String releaseTimeEnd;//发布时间结束
}
