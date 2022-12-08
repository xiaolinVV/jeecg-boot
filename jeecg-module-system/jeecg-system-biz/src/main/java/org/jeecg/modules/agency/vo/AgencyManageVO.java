package org.jeecg.modules.agency.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

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
public class AgencyManageVO {
    
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
	/**区域id*/
	private String sysAreaId;
	/**代理id*/
	private String sysUserId;
	/**订单佣金比例*/
	private BigDecimal orderCommissionRate;
	/**礼包佣金比例*/
	private BigDecimal giftCommissionRate;
	/**开店佣金比例*/
	private BigDecimal storeCommissionRate;
	/**福利金销售奖励*/
	private BigDecimal welfareCommissionRate;
	/**推荐供应商销售奖励*/
	private BigDecimal supplierSalesCommissionRate;
	/**兑换券销售奖励*/
	private BigDecimal cashCouponSalesIncentives;
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;
	/**备注*/
	private String remark;
	/**状态；0：停用；1：启用*/
	private String status;
	/**停用说明*/
	private String closeExplain;
	/**余额*/
	private BigDecimal balance;
	/**冻结金额（待结算）*/
	private BigDecimal accountFrozen;
	/**不可用金额*/
	private BigDecimal unusableFrozen;
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
	@Excel(name = "所在城市区域id", width = 15)
	@ApiModelProperty(value = "所在城市区域id")
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

	/**新增代码，临时数据*/
	/**头像*/
	private String  avatar;
	/**用户账号*/
	private String username;
	/**手机号*/
	private String phone;
	/**用户名字*/
	private String realname;
	/**代理地区*/
	private String sysAreaName;
	/**代理等级*/
	private String leve;
	/***代理Code*/
	private String roleCode;
	/***代理名称*/
	private String roleName;
	/**部门名称*/
	private String departName;
	/**查询条件代理开始时间*/
	private String startTime_begin;
	/**查询条件代理开始时间*/
	private String startTime_end;
	/**查询条件代理结束时间*/
	private String endTime_begin;
	/**查询条件代理结束时间*/
	private String endTime_end;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;

}
