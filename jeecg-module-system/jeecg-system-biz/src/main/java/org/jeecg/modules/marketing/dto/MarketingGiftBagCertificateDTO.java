package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class MarketingGiftBagCertificateDTO {
    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
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
    @TableLogic
    private String delFlag;
    /**兑换券id*/
    @Excel(name = "兑换券id", width = 15)
    @ApiModelProperty(value = "兑换券id")
    private String marketingCertificateId;
    /**礼包id*/
    @Excel(name = "礼包id", width = 15)
    @ApiModelProperty(value = "礼包id")
    private String marketingGiftBagId;
    /**优惠券名称*/
    @Excel(name = "优惠券名称", width = 15)
    @ApiModelProperty(value = "优惠券名称")
    private String name;
    /**用券时间方式；0：标准方式；1：领券当日起；2：领券次日起*/
    @Excel(name = "用券时间方式；0：标准方式；1：领券当日起；2：领券次日起", width = 15)
    @ApiModelProperty(value = "用券时间方式；0：标准方式；1：领券当日起；2：领券次日起")
    private String vouchersWay;
    /**开始时间*/
    @Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**结束时间*/
    @Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**多少天、周、月*/
    @Excel(name = "多少天、周、月", width = 15)
    @ApiModelProperty(value = "多少天、周、月")
    private BigDecimal disData;
    /**单位；天、周、月*/
    @Excel(name = "单位；天、周、月", width = 15)
    @ApiModelProperty(value = "单位；天、周、月")
    private String monad;
    /**商品数量*/
    @Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
    private BigDecimal shopTotal;
    /**使用人限制；会员类型数据字典*/
    @Excel(name = "使用人限制；会员类型数据字典", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "使用人限制；会员类型数据字典")
    @Dict(dicCode = "member_type")
    private String userRestrict;
    /**发放总量*/
    @Excel(name = "发放总量", width = 15)
    @ApiModelProperty(value = "发放总量")
    private BigDecimal total;
    /**核销奖励*/
    @Excel(name = "核销奖励", width = 15)
    @ApiModelProperty(value = "核销奖励")
    private BigDecimal theReward;
    /**
     * 适用商品统计
     */
    private String goodQuantity;
    /**
     * 核销门店统计
     */
    private String storeQuantity;
    /**
     * 已使用兑换券统计
     */
    private String certificateUse;
    /**
     * 已领取兑换券使用
     */
    private String certificateGet;
    /**
     * 用券时间
     */
    private String usrTime;

    /**
     * 券剩余发行量
     */
    private String discountSurplus;

    @Excel(name = "发放数量", width = 15)
    @ApiModelProperty(value = "发放数量")
    private BigDecimal distributedAmount;
    @Excel(name = "有效期控制；0：连续有效期；1：相同有效期", width = 15)
    @ApiModelProperty(value = "有效期控制；0：连续有效期；1：相同有效期")
    private String validityType;
    /**
     * 有效期控制
     */
    private String validityTypeName;
    //使用人限制
    private String memberTypeName;

    private String certificateType;
}
