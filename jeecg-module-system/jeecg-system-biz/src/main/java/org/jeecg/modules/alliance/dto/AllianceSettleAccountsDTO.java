package org.jeecg.modules.alliance.dto;

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
 * @Description: 加盟商提现
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@Data
@TableName("alliance_settle_accounts")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="alliance_settle_accounts对象", description="加盟商提现")
public class AllianceSettleAccountsDTO {
    
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
	/**加盟商id*/
	@Excel(name = "加盟商id", width = 15)
    @ApiModelProperty(value = "加盟商id")
	private String sysUserId;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
	private String orderNo;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phone;
	/**提现金额*/
	@Excel(name = "提现金额", width = 15)
    @ApiModelProperty(value = "提现金额")
	private java.math.BigDecimal money;
	/**提现类型；0：微信；1：支付宝；2：银行卡；*/
	@Excel(name = "提现类型；0：微信；1：支付宝；2：银行卡；", width = 15)
    @ApiModelProperty(value = "提现类型；0：微信；1：支付宝；2：银行卡；")
	private String withdrawalType;
	/**手续费*/
	@Excel(name = "手续费", width = 15)
    @ApiModelProperty(value = "手续费")
	private java.math.BigDecimal serviceCharge;
	/**实际金额*/
	@Excel(name = "实际金额", width = 15)
    @ApiModelProperty(value = "实际金额")
	private java.math.BigDecimal amount;
	/**结算时间*/
	@Excel(name = "结算时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结算时间")
	private Date timeApplication;
	/**状态；0：待审核；1：待打款；2：已付款；3：无效*/
	@Excel(name = "状态；0：待审核；1：待打款；2：已付款；3：无效", width = 15)
    @ApiModelProperty(value = "状态；0：待审核；1：待打款；2：已付款；3：无效")
	private String status;
	/**打款时间*/
	@Excel(name = "打款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "打款时间")
	private Date payTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**无效原因*/
	@Excel(name = "无效原因", width = 15)
    @ApiModelProperty(value = "无效原因")
	private String closeExplain;
	/**银行卡号(支付宝账号)*/
	@Excel(name = "银行卡号(支付宝账号)", width = 15)
    @ApiModelProperty(value = "银行卡号(支付宝账号)")
	private String bankCard;
	/**开户行名称*/
	@Excel(name = "开户行名称", width = 15)
    @ApiModelProperty(value = "开户行名称")
	private String bankName;
	/**持卡人姓名(真实姓名)*/
	@Excel(name = "持卡人姓名(真实姓名)", width = 15)
    @ApiModelProperty(value = "持卡人姓名(真实姓名)")
	private String cardholder;
	/**审核时间*/
	@Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
	/**
	 *用户账号
	 */
	private String username;
	/**
	 *用户名字
	 */
	private String realname;
	/**名称*/
	@Excel(name = "公司名称", width = 15)
	@ApiModelProperty(value = "公司名称")
	private String name;
	/**
	 *查询申请时间开始
	 */
	private String createTime_begin;
	/**
	 *查询申请时间结束
	 */
	private String createTime_end;
	/**
	 *查询审核时间开始
	 */
	private String auditTime_begin;
	/**
	 *查询审核时间结束
	 */
	private String auditTime_end;
	/**
	 *查询打款时间开始
	 */
	private String payTime_begin;
	/**
	 *查询打款时间结束
	 */
	private String payTime_end;
}
