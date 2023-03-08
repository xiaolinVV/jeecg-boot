package org.jeecg.modules.store.entity;

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

/**
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
@Data
@TableName("store_bank_card")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_bank_card对象", description="店铺银行卡")
public class StoreBankCard {
    
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
	@TableLogic
	private String delFlag;
	/**开户行名称*/
	@Excel(name = "开户银行", width = 15)
    @ApiModelProperty(value = "开户银行")
	private String bankName;
	/**银行卡号*/
	@Excel(name = "银行卡号", width = 15)
    @ApiModelProperty(value = "银行卡号")
	private String bankCard;
	/**持卡人*/
	@Excel(name = "户名（真实姓名）", width = 15)
    @ApiModelProperty(value = "户名（真实姓名）")
	private String cardholder;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
	/**卡类型；0：银行卡；1：支付宝*/
	@Excel(name = "卡类型；0：银行卡；1：支付宝", width = 15)
	@ApiModelProperty(value = "卡类型；0：银行卡；1：支付宝")
	private String carType;
	/**联系人手机号*/
	@Excel(name = "联系人手机号", width = 15)
	@ApiModelProperty(value = "联系人手机号")
	private String phone;
	/**身份证号*/
	@Excel(name = "证件号", width = 15)
	@ApiModelProperty(value = "证件号")
	private String identityNumber;
	/**所在城市*/
	@Excel(name = "所在城市", width = 15)
	@ApiModelProperty(value = "所在城市")
	private String citys;
	/**所在城市编码*/
	@Excel(name = "所在城市编码", width = 15)
	@ApiModelProperty(value = "所在城市编码")
	private String citysCode;
	/**开户分支行*/
	@Excel(name = "开户分支行", width = 15)
	@ApiModelProperty(value = "开户分支行")
	private String openingBank;

	/*
	* 汇付天下分账结算账户id
	* */
	private String settleAccount;

	private String bankCode;

	private String accountType;
}
