package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class MarketingDisountGoodDTO {
    private static final long serialVersionUID = 1L;
    /**主键ID*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;


    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic //mybatis-plus配置逻辑
    private String delFlag;
    /**
     * 店铺优惠券id
     */
    @Excel(name = "店铺优惠券id", width = 15)
    @ApiModelProperty(value = "店铺优惠券id")
    private String marketingDiscountId;
    /**
     * 店铺商品id
     */
    @Excel(name = "店铺商品id", width = 15)
    @ApiModelProperty(value = "店铺商品id")
    private String goodId;
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
    /**
     * 供应商
     */
    private String name;

}
