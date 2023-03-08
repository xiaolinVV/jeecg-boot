package org.jeecg.modules.store.dto;

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

import java.util.Date;

/**
 * @Description: 福利金收款商家
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
@Data
@TableName("store_welfare_payments_gathering")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_welfare_payments_gathering对象", description="福利金收款商家")
public class StoreWelfarePaymentsGatheringDTO {
    
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
	/**状态;0:停用；1：启用*/
	@Excel(name = "状态;0:停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态;0:停用；1：启用")
	private String status;
	/**店铺福利金抵扣最低值*/
	@Excel(name = "店铺福利金抵扣最低值", width = 15)
	@ApiModelProperty(value = "店铺福利金抵扣最低值")
	private String storeSmallWelfarePayments;
	/**兑换比例，百分比形式*/
	@Excel(name = "兑换比例，百分比形式", width = 15)
    @ApiModelProperty(value = "兑换比例，百分比形式")
	private java.math.BigDecimal subscriptionRatio;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**停用时间*/
	@Excel(name = "停用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "停用时间")
	private Date closeTime;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
	private String closeExplain;
	/**
	 * 店铺账号
	 */
	private String username;
	/**老板姓名（联系人名称）*/
	@Excel(name = "老板姓名（联系人名称）", width = 15)
	@ApiModelProperty(value = "老板姓名（联系人名称）")
	private String bossName;
	/**老板手机（联系人手机号，是登录账号）*/
	@Excel(name = "老板手机（联系人手机号，是登录账号）", width = 15)
	@ApiModelProperty(value = "老板手机（联系人手机号，是登录账号）")
	private String bossPhone;
	/**门店名称*/
	@Excel(name = "门店名称", width = 15)
	@ApiModelProperty(value = "门店名称")
	private String storeName;
	/**城市区域地址说明*/
	@Excel(name = "城市区域地址说明", width = 15)
	@ApiModelProperty(value = "城市区域地址说明")
	private String areaAddress;
	/**门店详细地址*/
	@Excel(name = "门店详细地址", width = 15)
	@ApiModelProperty(value = "门店详细地址")
	private String storeAddress;
	/**主营分类；建立数据库字典：store_main_type对应*/
	@Excel(name = "主营分类；建立数据库字典：store_main_type对应", width = 15,dicCode = "store_main_type")
	@ApiModelProperty(value = "主营分类；建立数据库字典：store_main_type对应")
	@Dict(dicCode = "store_main_type")
	private String mainType;
	//店铺分类id
	private String storeTypeId;

	private String createTime_begin;
	private String createTime_end;
}
