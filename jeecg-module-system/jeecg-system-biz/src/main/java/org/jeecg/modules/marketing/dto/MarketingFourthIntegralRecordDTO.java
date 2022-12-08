package org.jeecg.modules.marketing.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MarketingFourthIntegralRecordDTO {

    private String serialNumber;//流水号
    private String phone;//会员账号
    private String nickName;//会员昵称
    private String tradeType;//交易类型
    private String goAndCome;//收入/支出
    private String tradeNo;//交易单号
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTimeStart;//交易时间开始
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTimeEnd;//交易时间结束
}
