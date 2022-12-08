package org.jeecg.modules.good.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GoodDiscountDTO {
    /**
     *商品id
     */
    private String id;
    /**
     *商品图片
     */
    private String mainPicture;
    /**
     *商品名称
     */
    private String goodName;
    /**
     *商品分类
     */
    private String typeName;
    /**
     *市场价
     */
    private String marketPrice;
    /**
     *销售价
     */
    private String price;
    /**
     *成本价
     */
    private String costPrice;
    /**商品供货价*/
    private String supplyPrice;
    /**
     *会员价
     */
    private String vipPrice;
    /**
     *库存
     */
    private String repertory;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 供应商
     */
    private String name;
}
