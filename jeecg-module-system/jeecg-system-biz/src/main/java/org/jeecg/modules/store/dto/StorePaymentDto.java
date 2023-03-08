package org.jeecg.modules.store.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StorePaymentDto implements Serializable{
    private static final long serialVersionUID = 1L;
    /**主键ID*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**老板姓名（联系人名称）*/
    @Excel(name = "老板姓名（联系人名称）", width = 15)
    @ApiModelProperty(value = "老板姓名（联系人名称）")
    private String bossName;
    /**老板手机（联系人手机号，是登录账号）*/
    @Excel(name = "老板手机（联系人手机号，是登录账号）", width = 15)
    @ApiModelProperty(value = "老板手机（联系人手机号，是登录账号）")
    private String bossPhone;
    /**开通类型：0：包年；1：终生*/
    @Excel(name = "开通类型：0：包年；1：终生", width = 15)
    @ApiModelProperty(value = "开通类型：0：包年；1：终生")
    private String openType;
    /**开通费用*/
    @Excel(name = "开通费用", width = 15)
    @ApiModelProperty(value = "开通费用")
    private java.math.BigDecimal money;
    /**开通时间*/
    @Excel(name = "开通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开通时间")
    private Date startTime;
    /**到期时间，如果是终生就为空*/
    @Excel(name = "到期时间，如果是终生就为空", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到期时间，如果是终生就为空")
    private Date endTime;
    /**支付类型：0：微信；1：支付宝*/
    @Excel(name = "支付类型：0：微信；1：支付宝", width = 15)
    @ApiModelProperty(value = "支付类型：0：微信；1：支付宝")
    private String payType;
    /**支付时间*/
    @Excel(name = "支付时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private String delFlag;
    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
}
