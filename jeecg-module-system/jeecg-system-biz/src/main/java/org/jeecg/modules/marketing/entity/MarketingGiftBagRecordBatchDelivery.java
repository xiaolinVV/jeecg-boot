package org.jeecg.modules.marketing.entity;

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
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购礼包配送信息
 * @Author: jeecg-boot
 * @Date:   2020-09-04
 * @Version: V1.0
 */
@Data
@TableName("marketing_gift_bag_record_batch_delivery")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_gift_bag_record_batch_delivery对象", description="采购礼包配送信息")
@ExcelTarget("MarketingGiftBagRecordBatchDelivery")
public class MarketingGiftBagRecordBatchDelivery implements Serializable{
    
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
	private String delFlag;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
	private String batchNumber;
	/**父类，0为第一级*/
	@Excel(name = "父类，0为第一级", width = 15)
    @ApiModelProperty(value = "父类，0为第一级")
	private String pid;
	/**采购记录表id*/
	@Excel(name = "采购记录表id", width = 15)
    @ApiModelProperty(value = "采购记录表id")
	private String marketingGiftBagRecordBatchId;
	/**收件人*/
	@Excel(name = "收件人", width = 15)
    @ApiModelProperty(value = "收件人")
	private String linkman;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phone;
	/**区域描述*/
	@Excel(name = "所在城市", width = 15)
    @ApiModelProperty(value = "区域描述")
	private String areaExplan;
	/**详细地址*/
	@Excel(name = "详细收货地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private String areaAddress;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；*/
	@Excel(name = "快递公司", width = 15,dicCode = "logistics_company")
    @ApiModelProperty(value = "物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；")
	private String logisticsCompany;
	/**快递单号*/
	@Excel(name = "快递单号", width = 15)
    @ApiModelProperty(value = "快递单号")
	private String trackingNumber;
}
