package org.jeecg.modules.agency.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Data
@TableName("agency_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="agency_manage对象", description="代理列表")
public class AgencyManage {
    
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
	/**区域id*/
	@Excel(name = "区域id", width = 15)
    @ApiModelProperty(value = "区域id")
	private String sysAreaId;
	/**代理id*/
	@Excel(name = "代理id", width = 15)
    @ApiModelProperty(value = "代理id")
	private String sysUserId;
	/**订单佣金比例*/
	@Excel(name = "订单佣金比例", width = 15)
    @ApiModelProperty(value = "订单佣金比例")
	private BigDecimal orderCommissionRate;
	/**礼包佣金比例*/
	@Excel(name = "礼包佣金比例", width = 15)
    @ApiModelProperty(value = "礼包佣金比例")
	private BigDecimal giftCommissionRate;
	/**开店佣金比例*/
	@Excel(name = "开店佣金比例", width = 15)
    @ApiModelProperty(value = "开店佣金比例")
	private BigDecimal storeCommissionRate;
	/**开始时间*/
	@Excel(name = "开始时间", width = 10, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")//timezone = "GMT+8",
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 10, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")//timezone = "GMT+8",
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
	private Date endTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;
	/**余额*/
	@Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
	private BigDecimal balance;
	/**冻结金额（待结算）*/
	@Excel(name = "冻结金额（待结算）", width = 15)
    @ApiModelProperty(value = "冻结金额（待结算）")
	private BigDecimal accountFrozen;
	/**不可用金额*/
	@Excel(name = "不可用金额", width = 15)
    @ApiModelProperty(value = "不可用金额")
	private BigDecimal unusableFrozen;
	/**福利金销售奖励*/
	@Excel(name = "福利金销售奖励", width = 15)
	@ApiModelProperty(value = "福利金销售奖励")
	private BigDecimal welfareCommissionRate;
	/**推荐供应商销售奖励*/
	@Excel(name = "推荐供应商销售奖励", width = 15)
	@ApiModelProperty(value = "推荐供应商销售奖励")
	private BigDecimal supplierSalesCommissionRate;

	/**兑换券销售奖励*/
	@Excel(name = "兑换券销售奖励", width = 15)
	@ApiModelProperty(value = "兑换券销售奖励")
	private BigDecimal cashCouponSalesIncentives;
	/**推广人佣金比例*/
	@Excel(name = "推广人佣金比例", width = 15)
	@ApiModelProperty(value = "推广人佣金比例")
	private BigDecimal promoterCommissionRate;
	/**代理公司名称*/
	@Excel(name = "代理公司名称", width = 15)
	@ApiModelProperty(value = "代理公司名称")
	private String name;
	/**
	 *所在城市区域id
	 */
	@Excel(name = "所在城市区域id", width = 15)
	@ApiModelProperty(value = "所在城市区域id")
	private String agencyAreaId;
	/**
	 *所在城市区域id
	 */
	@Excel(name = "所在城市区域说明", width = 15)
	@ApiModelProperty(value = "所在城市区域说明")
	private String areaAddress;
	/**
	 *详细地址
	 */
	@Excel(name = "详细地址", width = 15)
	@ApiModelProperty(value = "详细地址")
	private String address;
	/**
	 *公司手机号码
	 */
	@Excel(name = "公司手机号码", width = 15)
	@ApiModelProperty(value = "公司手机号码")
	private String companyPhone;

}
