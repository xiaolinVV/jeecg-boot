package org.jeecg.modules.marketing.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MarketingDisplayVO implements Serializable {
    /**
     *时间
     */
    @ApiModelProperty(value = "时间")
    private Integer myDate;
    /**
     *金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal totalPrice;

}
