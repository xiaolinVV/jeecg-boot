package org.jeecg.modules.store.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 福利金收款记录
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
@Data
@TableName("store_welfare_payments_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_welfare_payments_record对象", description="福利金收款记录")
public class StoreWelfarePaymentsRecordVO {
    
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
	private String delFlag;
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**福利金金额*/
	@Excel(name = "福利金金额", width = 15)
    @ApiModelProperty(value = "福利金金额")
	private java.math.BigDecimal welfarePayments;
	/**收款金额*/
	@Excel(name = "收款金额", width = 15)
    @ApiModelProperty(value = "收款金额")
	private java.math.BigDecimal gatheringMoney;
	/**状态；0：未完成；1：交易完成*/
	@Excel(name = "状态；0：未完成；1：交易完成", width = 15)
    @ApiModelProperty(value = "状态；0：未完成；1：交易完成")
	private String status;
	/**交易流水号*/
	@Excel(name = "交易单号", width = 15)
    @ApiModelProperty(value = "交易单号")
	private String tradeNo;
	/**收款说明*/
	@Excel(name = "收款说明", width = 15)
    @ApiModelProperty(value = "收款说明")
	private String gatheringExplain;
	@Excel(name = "流水号", width = 15)
	@ApiModelProperty(value = "流水号")
	private String orderNo;
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 店铺账号
	 */
	private String username;
	/**
	 * 店铺名称
	 */
	private String storeName;
	/**
	 * 用户名称
	 */
	private String nickName;
	/**
	 * 用户账号
	 */
	private String phone;
}
