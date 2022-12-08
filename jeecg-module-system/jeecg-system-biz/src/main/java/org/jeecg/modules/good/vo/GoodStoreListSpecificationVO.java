package org.jeecg.modules.good.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 规格Vo
 */
@Data
public class GoodStoreListSpecificationVO {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;
    /**商品销售价格*/
    private BigDecimal price;
    /**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
    private BigDecimal vipPrice;
    private BigDecimal costPrice;
    /**库存*/
    private BigDecimal repertory;
    /**sku编码*/
    private String skuNo;
    /**重量*/
    private BigDecimal weight;
    /**规格*/
    private String specification;
    /**规格下标*/
    private String key;
    /**规格图*/
    private String specificationPicture;
    /**销量*/
    private String salesVolume;
}
