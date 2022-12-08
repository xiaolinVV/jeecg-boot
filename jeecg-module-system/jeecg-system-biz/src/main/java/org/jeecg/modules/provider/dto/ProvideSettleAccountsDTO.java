package org.jeecg.modules.provider.dto;

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
 * @Description: 供应商待付款
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
@Data
@TableName("provide_settle_accounts")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="provide_settle_accounts对象", description="供应商待付款")
public class ProvideSettleAccountsDTO {

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
	/**供应商id*/
	private String sysUserId;
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
	//收款账号拼接
	private String jointName;

	/**新增代码*/
	/**供应商号码*/
	private String Phone;
	/**客户名称*/
	private String userName;
	/**供应商名称*/
	private String providerName;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**供应商创建年*/
	private String  createYear;
}
