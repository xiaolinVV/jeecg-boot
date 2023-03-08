package org.jeecg.modules.provider.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 供应商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
@Data
@TableName("provider_recharge_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="provider_recharge_record对象", description="供应商余额明细")
public class ProviderRechargeRecordVO {

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
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
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
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id")
	private String sysUserId;
	/**交易类型；0：订单交易；1：余额提现；2：订单退款；3：余额充值；对应数据字典*/
	@Excel(name = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：余额充值；对应数据字典", width = 15)
    @ApiModelProperty(value = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：余额充值；对应数据字典")
	@Dict(dicCode = "provide_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	@Excel(name = "支付和收入；0：收入；1：支出", width = 15)
    @ApiModelProperty(value = "支付和收入；0：收入；1：支出")
	private String goAndCome;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private java.math.BigDecimal amount;
	/**交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭*/
	@Excel(name = "交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭", width = 15)
    @ApiModelProperty(value = "交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭")
    @Dict(dicCode = "trade_status")
    private String tradeStatus;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
	private String orderNo;
	/**供应商银行卡id*/
	@Excel(name = "供应商银行卡id", width = 15)
    @ApiModelProperty(value = "供应商银行卡id")
	private String providerBankCardId;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
	private String operator;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**支付方式；0:微信支付；1：支付宝支付*/
	@Excel(name = "支付方式；0:微信支付；1：支付宝支付", width = 15)
    @ApiModelProperty(value = "支付方式；0:微信支付；1：支付宝支付")
	private String payment;

	/**新增代码*/
	/**供应商号码*/
	private String linkphone;
	/**供应商名称*/
	private String providerName ;
	/**老板名称*/
	private String linkman ;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**供应商创建年*/
	private String  createYear;
	/**供应商银行卡信息*/
	private String bankCard;

	private String userName;

}
