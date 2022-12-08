package org.jeecg.modules.store.dto;

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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 店铺余额记录
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Data
@TableName("store_recharge_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_recharge_record对象", description="店铺余额记录")
public class StoreRechargeRecordDTO {

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
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
	/**交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典*/
	@Excel(name = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典", width = 15,dicCode = "store_deal_type")
    @ApiModelProperty(value = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典")
	@Dict(dicCode = "store_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	@Excel(name = "支付和收入；0：收入；1：支出", width = 15)
    @ApiModelProperty(value = "支付和收入；0：收入；1：支出")
	private String goAndCome;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
	private BigDecimal amount;
	/**交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭*/
	@Excel(name = "交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭", width = 15,dicCode = "trade_status")
    @ApiModelProperty(value = "交易状态;0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭")
	@Dict(dicCode = "trade_status")
	private String tradeStatus;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
	private String orderNo;
	/**店铺银行卡id*/
	@Excel(name = "店铺银行卡id", width = 15)
    @ApiModelProperty(value = "店铺银行卡id")
	private String storeBankCardId;
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
	/**交易单号*/
	@Excel(name = "交易单号", width = 15)
	@ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**交易类型；0：订单；1：礼包*/
	@Excel(name = "**交易类型；0：订单；1：礼包", width = 15)
	@ApiModelProperty(value = "**交易类型；0：订单；1：礼包")
	private String tradeType;
	@Excel(name = "支付日志",width = 15)
	@ApiModelProperty(value = "支付日志")
	private String payParam;
	@Excel(name = "回调状态；0：未回调；1：已回调",width = 15)
	@ApiModelProperty(value = "回调状态；0：未回调；1：已回调")
	private String backStatus;
	@Excel(name = "回调次数",width = 15)
	@ApiModelProperty(value = "回调次数")
	private BigDecimal backTimes;
	@Excel(name = "银行卡号(支付宝账号)",width = 15)
	@ApiModelProperty(value = "银行卡号(支付宝账号)")
	private String bankCard;
	@Excel(name = "开户行名称",width = 15)
	@ApiModelProperty(value = "开户行名称")
	private String bankName;
	@Excel(name = "持卡人姓名(真实姓名)",width = 15)
	@ApiModelProperty(value = "持卡人姓名(真实姓名)")
	private String cardholder;

	/**
	 * 可用余额
	 */
	private String balanceHandSum;
	/**
	 * 待结算
	 */
	private String balanceWaitSum;
	/**
	 * 不可用余额
	 */
	private String balanceNoSum;


	/**新增代码*/
	/**店铺号码*/
	private String bossPhone;
	/**店铺名称*/
	private String storeName;
	/**老板名称*/
	private String bossName;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**店铺Id*/
	private String sysUserId;
	/**店铺创建年*/
	private String  createYear;
	/**
	 * 店铺账号
	 */
	private String userName;
}
