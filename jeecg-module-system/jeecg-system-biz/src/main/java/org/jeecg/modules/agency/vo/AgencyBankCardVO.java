package org.jeecg.modules.agency.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 代理银行卡
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
@Data
@TableName("agency_bank_card")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="agency_bank_card对象", description="代理银行卡")
public class AgencyBankCardVO {
    
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
	private String delFlag;
	/**开户行名称*/
	@Excel(name = "开户行名称", width = 15)
    @ApiModelProperty(value = "开户行名称")
	private String bankName;
	/**银行卡号（支付宝账号）*/
	@Excel(name = "银行卡号（支付宝账号）", width = 15)
    @ApiModelProperty(value = "银行卡号（支付宝账号）")
	private String bankCard;
	/**持卡人（真实姓名）*/
	@Excel(name = "持卡人（真实姓名）", width = 15)
    @ApiModelProperty(value = "持卡人（真实姓名）")
	private String cardholder;
	/**代理id*/
	@Excel(name = "代理id", width = 15)
    @ApiModelProperty(value = "代理id")
	private String sysUserId;
	/**卡类型；0：银行卡；1：支付宝*/
	@Excel(name = "卡类型；0：银行卡；1：支付宝", width = 15)
    @ApiModelProperty(value = "卡类型；0：银行卡；1：支付宝")
	private String carType;
	/**变更说明*/
	@Excel(name = "变更说明", width = 15)
	@ApiModelProperty(value = "变更说明")
	private String updateExplain;
	/**变更凭证*/
	@Excel(name = "变更凭证", width = 15)
	@ApiModelProperty(value = "变更凭证")
	private String updateCertificate;
	/**联系人手机号*/
	@Excel(name = "联系人手机号", width = 15)
	@ApiModelProperty(value = "联系人手机号")
	private String phone;
	//验证码
	private String sbCode;
	/**
	 * 拼接银行卡信息
	 */
	private String sbcName;
	/**
	 * 银行卡id
	 */
	private String agencyBankCardId;

	//头像
	private String avatar;
	//账号
	private String username;
	//名称
	private String name;
	//变更凭证第一张图片
	private String updateCertificateOne;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
	@ApiModelProperty(value = "身份证号")
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
}
