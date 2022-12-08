package org.jeecg.modules.member.dto;

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
 * @Description: 会员提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Data
@TableName("member_withdraw_deposit")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_withdraw_deposit对象", description="会员提现审批")
public class MemberWithdrawDepositDTO {

	/**主键ID*/
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	private Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	private Date updateTime;
	/**创建年*/
	private Integer year;
	/**创建月*/
	private Integer month;
	/**创建日*/
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;
	/**会员id*/
	private String memberListId;
	/**单号*/
	private String orderNo;
	/**手机号*/
	private String phone;
	/**提现金额*/
	private java.math.BigDecimal money;
	/**提现类型；0：微信；1：支付宝*/
	private String withdrawalType;
	/**手续费*/
	private java.math.BigDecimal serviceCharge;
	/**实际金额*/
	private java.math.BigDecimal amount;
	/**申请时间*/
	private Date timeApplication;
	/**状态；0：待审核；1：待打款；2：已付款；3：无效*/
	private String status;
	/**打款时间*/
	private Date payTime;
	/**备注*/
	private String remark;
	/**无效原因*/
	private String closeExplain;
	@Excel(name = "银行卡号(支付宝账号)",width = 15)
	@ApiModelProperty(value = "银行卡号(支付宝账号)")
	private String bankCard;
	@Excel(name = "开户行名称",width = 15)
	@ApiModelProperty(value = "开户行名称")
	private String bankName;
	@Excel(name = "持卡人姓名(真实姓名)",width = 15)
	@ApiModelProperty(value = "持卡人姓名(真实姓名)")
	private String cardholder;
	@Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "审核时间")
	private Date auditTime;
	@Excel(name = "收款凭证",width = 15)
	@ApiModelProperty(value = "收款凭证")
	private String receiptVoucher;
	@Excel(name = "处理信息进度json",width = 15)
	@ApiModelProperty(value = "处理信息进度json")
	private String processInformation;
	/**新增临时数据*/
	private String nickName;
	/**查询条件开始时间*/
	private String timeApplication_begin;
	/**查询条件开始时间*/
	private String timeApplication_end;
	//提现账号拼接信息
	private String bankCardName;
	/**归属店铺Id*/
	private String sysUserId;
	/**
	 * 开户分支行
	 */
	private String openingBank;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
	@ApiModelProperty(value = "身份证号")
	private String identityNumber;
}
