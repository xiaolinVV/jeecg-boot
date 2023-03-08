package org.jeecg.modules.member.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 会员福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@Data
@TableName("member_welfare_payments")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_welfare_payments对象", description="会员福利金")
public class MemberWelfarePayments {
    
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
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**交易福利金*/
	@Excel(name = "交易福利金", width = 15)
    @ApiModelProperty(value = "交易福利金")
	private java.math.BigDecimal bargainPayments;
	/**账户福利金*/
	@Excel(name = "账户福利金", width = 15)
    @ApiModelProperty(value = "账户福利金")
	private java.math.BigDecimal welfarePayments;
	/**类型；0：支出；1：收入*/
	@Excel(name = "类型；0：支出；1：收入", width = 15)
    @ApiModelProperty(value = "类型；0：支出；1：收入")
	private String weType;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
	private String wpExplain;
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
	@Excel(name = "0：店铺；1：平台", width = 15)
	@ApiModelProperty(value = "0：店铺；1：平台")
	private String isPlatform;
	@Excel(name = "是否冻结福利金，0:不是；1：是冻结；2：不可用", width = 15)
	@ApiModelProperty(value = "是否冻结福利金，0:不是；1：是冻结；2：不可用")
	private String isFreeze;
	@Excel(name = "交易订单号", width = 15)
	@ApiModelProperty(value = "交易订单号")
	private String tradeNo;
	@Excel(name = "交易类型；0：订单；1：礼包；2：平台；3：店铺；4：会员；5：会员送出；6：开店", width = 15,dicCode = "member_welfare_deal_type")
	@ApiModelProperty(value = "会员福利金交易类型，字典member_welfare_deal_type；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；")
	@Dict(dicCode = "member_welfare_deal_type")
	private String tradeType;
	@Excel(name = "交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status", width = 15,dicCode = "trade_status")
	@ApiModelProperty(value = "交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status")
	@Dict(dicCode = "trade_status")
	private String tradeStatus;

	@Version
	private Integer version;
	/*备注*/
	private String remark;
}
