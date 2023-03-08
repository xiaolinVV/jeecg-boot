package org.jeecg.modules.marketing.entity;

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

/**
 * @Description: 店铺会员礼品卡
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_store_gift_card_member_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_store_gift_card_member_list对象", description="店铺会员礼品卡")
public class MarketingStoreGiftCardMemberList {
    
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
	@TableLogic
	private String delFlag;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**礼品卡列表id*/
	@Excel(name = "礼品卡列表id", width = 15)
    @ApiModelProperty(value = "礼品卡列表id")
	private String marketingStoreGiftCardListId;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private String serialNumber;
	/**获得时间*/
	@Excel(name = "获得时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "获得时间")
	private java.util.Date getTime;
	/**店铺列表id*/
	@Excel(name = "店铺列表id", width = 15)
    @ApiModelProperty(value = "店铺列表id")
	private String storeManageId;
	/**卡名称*/
	@Excel(name = "卡名称", width = 15)
    @ApiModelProperty(value = "卡名称")
	private String carName;
	/**面额*/
	@Excel(name = "面额", width = 15)
    @ApiModelProperty(value = "面额")
	private java.math.BigDecimal denomination;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private java.util.Date endTime;
	/**状态；0：已失效；1：可使用；2：已赠送*/
	@Excel(name = "状态；0：已失效；1：可使用；2：已赠送", width = 15)
    @ApiModelProperty(value = "状态；0：已失效；1：可使用；2：已赠送")
	private String status;

	/**
	 * 获得方式；0：礼包赠送；1：好友赠送
	 */
	private String waysObtain;
	/**
	 * 赠送人id
	 */
	private String tMemberId;

	/**
	 * 是否领取，0：未领取；1：已领取
	 */
	private String ifGet;
}
