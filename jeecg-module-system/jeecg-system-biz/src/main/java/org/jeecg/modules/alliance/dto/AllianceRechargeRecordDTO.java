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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 加盟商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Data
@TableName("alliance_recharge_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="alliance_recharge_record对象", description="加盟商余额明细")
public class AllianceRechargeRecordDTO {

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
	/**交易类型：0：订单交易；1：财务结算；2：订单退款；3：开店奖励；4：礼包分成；5：福利金销售奖励；6：兑换券销售奖励；7：推荐供应商销售奖励；做成数据字典agency_deal_type*/
	@Excel(name = "交易类型：0：订单交易；1：财务结算；2：订单退款；3：开店奖励；4：礼包分成；5：福利金销售奖励；6：兑换券销售奖励；7：推荐供应商销售奖励；8：推荐开店奖励；做成数据字典alliance_deal_type", width = 15,dicCode = "alliance_deal_type")
	@ApiModelProperty(value = "交易类型：0：订单交易；1：财务结算；2：订单退款；3：开店奖励；4：礼包分成；5：福利金销售奖励；6：兑换券销售奖励；7：推荐供应商销售奖励；8：推荐开店奖励；做成数据字典alliance_deal_type")
	@Dict(dicCode = "alliance_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	@Excel(name = "支付和收入；0：收入；1：支出", width = 15)
	@ApiModelProperty(value = "支付和收入；0：收入；1：支出")
	private String goAndCome;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
	@ApiModelProperty(value = "交易金额")
	private java.math.BigDecimal amount;
	/**单号*/
	@Excel(name = "单号", width = 15)
	@ApiModelProperty(value = "单号")
	private String orderNo;
	/**交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status*/
	@Excel(name = "交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status", width = 15,dicCode = "trade_status")
	@ApiModelProperty(value = "交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status")
	@Dict(dicCode = "trade_status")
	private String tradeStatus;
	/**支付方式；0:微信支付；1：支付宝支付*/
	@Excel(name = "支付方式；0:微信支付；1：支付宝支付", width = 15)
	@ApiModelProperty(value = "支付方式；0:微信支付；1：支付宝支付")
	private String payment;
	/**交易单号*/
	@Excel(name = "交易单号", width = 15)
	@ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**交易类型；0：订单；1：礼包；2：兑换券*/
	@Excel(name = "交易类型；0：订单；1：礼包；2：兑换券", width = 15)
	@ApiModelProperty(value = "交易类型；0：订单；1：礼包；2：兑换券")
	private String tradeType;
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
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
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
	 * 查询条件开始
	 */
	private String createTime_begin;
	/**
	 * 查询条件结束
	 */
	private String createTime_end;
}
