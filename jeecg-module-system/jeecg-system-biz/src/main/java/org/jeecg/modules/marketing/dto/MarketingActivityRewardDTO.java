package org.jeecg.modules.marketing.dto;

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
 * @Description: 活动奖励
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Data
@TableName("marketing_activity_reward")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_activity_reward对象", description="活动奖励")
public class MarketingActivityRewardDTO {
    
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
	/**店铺管理id*/
	@Excel(name = "店铺管理id", width = 15)
    @ApiModelProperty(value = "店铺管理id")
	private String storeManageId;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**进店活动id*/
	@Excel(name = "进店活动id", width = 15)
    @ApiModelProperty(value = "进店活动id")
	private String marketingCommingStoreId;
	/**奖品类型；0：福利金；1：优惠券*/
	@Excel(name = "奖品类型；0：福利金；1：优惠券", width = 15)
    @ApiModelProperty(value = "奖品类型；0：福利金；1：优惠券")
	private String awardType;
	/**奖品*/
	@Excel(name = "奖品", width = 15)
    @ApiModelProperty(value = "奖品")
	private String award;
	/**发放状态；0：发放失败；1：发放成功*/
	@Excel(name = "发放状态；0：发放失败；1：发放成功", width = 15)
    @ApiModelProperty(value = "发放状态；0：发放失败；1：发放成功")
	private String status;
	/**失败原因*/
	@Excel(name = "失败原因", width = 15)
    @ApiModelProperty(value = "失败原因")
	private String failureExplain;
	/**补发说明*/
	@Excel(name = "补发说明", width = 15)
    @ApiModelProperty(value = "补发说明")
	private String reissueExplain;
	/**补发说明*/
	@Excel(name = "补发说明", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "补发说明")
	private Date reissueTime;
	/**补发凭证*/
	@Excel(name = "补发凭证", width = 15)
    @ApiModelProperty(value = "补发凭证")
	private String reissueCertificate;
	/**补发人*/
	@Excel(name = "补发人", width = 15)
    @ApiModelProperty(value = "补发人")
	private String reissuePeople;
	//活动名称
	private String title;

	private String storeName;

	private String phone;

	private String nickName;

	private String createTime_begin;

	private String createTime_end;

	private String updateTime_begin;

	private String updateTime_end;

	private String sysUserId;
}
