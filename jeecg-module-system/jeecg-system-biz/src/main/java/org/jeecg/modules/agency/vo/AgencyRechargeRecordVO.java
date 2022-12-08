package org.jeecg.modules.agency.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Data
@TableName("agency_recharge_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="agency_recharge_record对象", description="代理列表")
public class AgencyRechargeRecordVO {

	/**主键ID*/
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	private java.util.Date updateTime;
	/**创建年*/
	private Integer year;
	/**创建月*/
	private Integer month;
	/**创建日*/
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;
	/**代理id*/
	private String sysUserId;
	/**交易类型；0：订单交易；1：提现；2：订单退款；3：余额充值；做成数据字典*/
	@Dict(dicCode = "agency_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	private String goAndCome;
	/**交易金额*/
	private java.math.BigDecimal amount;
	/**单号*/
	private String orderNo;
	/**交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭*/
	@Dict(dicCode = "trade_status")
	private String tradeStatus;
	/**支付方式；0:微信支付；1：支付宝支付*/
	@Excel(name = "支付方式；0:微信支付；1：支付宝支付", width = 15)
	@ApiModelProperty(value = "支付方式；0:微信支付；1：支付宝支付")
	private String payment;
	/**交易单号*/
	@Excel(name = "交易单号",width = 15)
	@ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**交易类型；0：订单；1：礼包*/
	@Excel(name = "交易类型；0：订单；1：礼包",width = 15)
	@ApiModelProperty(value = "交易类型；0：订单；1：礼包")
	private String tradeType;
	@Excel(name = "银行卡号(支付宝账号)",width = 15)
	@ApiModelProperty(value = "银行卡号(支付宝账号)")
	private String bankCard;
	@Excel(name = "开户行名称",width = 15)
	@ApiModelProperty(value = "开户行名称")
	private String bankName;
	@Excel(name = "持卡人姓名(真实姓名)",width = 15)
	@ApiModelProperty(value = "持卡人姓名(真实姓名)")
	private String cardholder;
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;

	/**供应商号码*/
	private String Phone;
	/**
	 * 代理
	 */
	private String name;
	/**客户名称*/
	private String userName;
	/**代理余额名称*/
	private String balance;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**供应商创建年*/
	private String  createYear;
}
