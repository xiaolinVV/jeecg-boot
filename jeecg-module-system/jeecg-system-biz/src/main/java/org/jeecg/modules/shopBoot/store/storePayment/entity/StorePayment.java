package org.jeecg.modules.shopBoot.store.storePayment.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 店铺消费记录
 * @Author: jeecg-boot
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("store_payment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="store_payment对象", description="店铺消费记录")
public class StorePayment implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private java.lang.Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private java.lang.Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private java.lang.Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    private java.lang.String delFlag;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
    private java.lang.String storeManageId;
	/**老板手机（联系人手机号，是登录账号）*/
	@Excel(name = "老板手机（联系人手机号，是登录账号）", width = 15)
    @ApiModelProperty(value = "老板手机（联系人手机号，是登录账号）")
    private java.lang.String bossPhone;
	/**开通类型：0：包年；1：终生*/
	@Excel(name = "开通类型：0：包年；1：终生", width = 15, dicCode = "store_payment_open_type")
	@Dict(dicCode = "store_payment_open_type")
    @ApiModelProperty(value = "开通类型：0：包年；1：终生")
    private java.lang.String openType;
	/**开通费用*/
	@Excel(name = "开通费用", width = 15)
    @ApiModelProperty(value = "开通费用")
    private java.math.BigDecimal money;
	/**开通时间*/
	@Excel(name = "开通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开通时间")
    private java.util.Date startTime;
	/**到期时间，如果是终生就为空*/
	@Excel(name = "到期时间，如果是终生就为空", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到期时间，如果是终生就为空")
    private java.util.Date endTime;
	/**支付状态：0：未支付；1：已支付*/
	@Excel(name = "支付状态：0：未支付；1：已支付", width = 15)
    @ApiModelProperty(value = "支付状态：0：未支付；1：已支付")
    private java.lang.String payStatus;
	/**支付类型：0：微信；1：支付宝*/
	@Excel(name = "支付类型：0：微信；1：支付宝", width = 15, dicCode = "pay_type")
	@Dict(dicCode = "pay_type")
    @ApiModelProperty(value = "支付类型：0：微信；1：支付宝")
    private java.lang.String payType;
	/**支付时间*/
	@Excel(name = "支付时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private java.util.Date payTime;
}
