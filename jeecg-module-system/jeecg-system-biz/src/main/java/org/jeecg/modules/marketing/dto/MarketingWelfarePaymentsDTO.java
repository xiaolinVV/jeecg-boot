package org.jeecg.modules.marketing.dto;

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
 * @Description: 店铺福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@Data
@TableName("marketing_welfare_payments")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_welfare_payments对象", description="店铺福利金")
public class MarketingWelfarePaymentsDTO {

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
	/**店铺福利金id*/
	@Excel(name = "店铺福利金id", width = 15)
    @ApiModelProperty(value = "店铺福利金id")
	private String sysUserId;
	/**会员列表，如果没有赠送就为空*/
	@Excel(name = "会员列表，如果没有赠送就为空", width = 15)
    @ApiModelProperty(value = "会员列表，如果没有赠送就为空")
	private String memberListId;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**交易福利金*/
	@Excel(name = "交易福利金", width = 15)
    @ApiModelProperty(value = "交易福利金")
	private BigDecimal bargainPayments;
	/**账户福利金*/
	@Excel(name = "账户福利金", width = 15)
    @ApiModelProperty(value = "账户福利金")
	private BigDecimal welfarePayments;
	/**类型；0：支出；1：收入*/
	@Excel(name = "类型；0：支出；1：收入", width = 15)
    @ApiModelProperty(value = "类型；0：支出；1：收入")
	private String weType;
	/**说明*/
	@Excel(name = "赠送说明", width = 15)
    @ApiModelProperty(value = "赠送说明")
	private String giveExplain;
	/**来源或者去向*/
	@Excel(name = "来源或者去向", width = 15)
    @ApiModelProperty(value = "来源或者去向")
	private String goAndCome;
	/**交易时间*/
	@Excel(name = "交易时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间")
	private Date bargainTime;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
	private String operator;
	/**店铺账户余额*/
	@Excel(name = "店铺账户余额", width = 15)
    @ApiModelProperty(value = "店铺账户余额")
	private BigDecimal balance;
	/**
	 * 福利金支付
	 */
	@Excel(name = "福利金支付", width = 15)
	@ApiModelProperty(value = "福利金支付")
	private BigDecimal welfarePay;
	/**
	 * 余额支付
	 */
	@Excel(name = "余额支付", width = 15)
	@ApiModelProperty(value = "余额支付")
	private BigDecimal balancePay;
	@Excel(name = "交易状态:1,成功", width = 15)
	@ApiModelProperty(value = "交易状态:1,成功")
	private String status;
	@Excel(name = "支付方式；1:余额；2：店铺赠送；3:平台礼包", width = 15)
	@ApiModelProperty(value = "支付方式；1:余额；2：店铺赠送；3:平台礼包")
	private String payMode;
	@Excel(name = "赠送人", width = 15)
	@ApiModelProperty(value = "赠送人")
	private String sendUser;
	@Excel(name = "0,店铺; 1,平台", width = 15)
	@ApiModelProperty(value = "0,店铺; 1,平台")
	private String isPlatform;
	@Excel(name = "0：商城会员；1：店铺管理员", width = 15)
	@ApiModelProperty(value = "0：商城会员；1：店铺管理员")
	private String userType;
	@Excel(name = "交易单号", width = 15)
	@ApiModelProperty(value = "交易单号")
	private String tradeNo;
	@Excel(name = "交易类型；0：订单；1：礼包；2：平台；3：店铺；4：会员；5：会员送出；6：开店", width = 15,dicCode = "welfare_deal_type")
	@ApiModelProperty(value = "交易类型；0：订单；1：礼包；2：平台；3：店铺；4：会员；5：会员送出；6：开店")
	@Dict(dicCode = "welfare_deal_type")
	private String tradeType;
	//用户类型
	private String typeName;
	//获赠账号
	private String phoneP;
	//获赠会员账号
	private String memberPhone;
	//店铺名称
	private String bossName;
	//店铺号码
	private String bossPhone;
	private String storeName;
	//交易状态名称
	private String statusName;
	//赠送方式
	private String payModeName;
	private String bargainTime_begin;
	private String bargainTime_end;
	private String uId;
	private String phone;

	private String username;
}
