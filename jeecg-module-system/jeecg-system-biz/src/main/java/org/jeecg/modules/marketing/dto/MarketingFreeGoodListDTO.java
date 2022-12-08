package org.jeecg.modules.marketing.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MarketingFreeGoodListDTO {
    private String goodNo;//商品编号
    private String goodName;//商品名称
    private String goodTypeId;//商品分类id
    private String marketingFreeGoodTypeId;//活动商品分类id
    private Date createTimeStart;//创建时间开始
    private Date createTimeEnd;//创建时间结束
    private Date updateTimeStart;//更新时间开始
    private Date updateTimeEnd;//更新时间结束
    private String isRecommend;//是否推荐；0：否；1：是
    private String status;//状态：0：停用；1：启用

}
