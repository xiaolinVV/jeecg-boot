package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("marketing_discount_good")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_discount_good对象", description="优惠券and店铺商品中间表")
public class MarketingDiscountGood {

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**修改人*/
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**修改时间*/
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**创建年*/
    @Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**创建月*/
    @Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**创建日*/
    @Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private Integer day;
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
    @Excel(name = "优惠券分类: 0:店铺 1:平台", width = 15)
    @ApiModelProperty(value = "优惠券分类: 0:店铺 1:平台")
    private String isPlatform;
}
