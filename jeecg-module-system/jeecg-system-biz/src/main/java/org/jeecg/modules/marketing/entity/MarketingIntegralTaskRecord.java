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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 积分任务记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_integral_task_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_integral_task_record对象", description="积分任务记录")
public class MarketingIntegralTaskRecord {
    
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
	private String delFlag;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**积分任务id*/
	@Excel(name = "积分任务id", width = 15)
    @ApiModelProperty(value = "积分任务id")
	private String marketingIntegralTaskId;
	/**奖励*/
	@Excel(name = "奖励", width = 15)
    @ApiModelProperty(value = "奖励")
	private java.math.BigDecimal award;
	/**状态：0：未领取；1：已领取*/
	@Excel(name = "状态：0：未领取；1：已领取", width = 15)
    @ApiModelProperty(value = "状态：0：未领取；1：已领取")
	private String status;
	/**领取时间*/
	@Excel(name = "领取时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "领取时间")
	private java.util.Date drawTime;
	/**任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告*/
	@Excel(name = "任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告", width = 15,dicCode = "task_type")
	@ApiModelProperty(value = "任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告")
	@Dict(dicCode = "task_type")
	private String taskType;
	/**任务标题*/
	@Excel(name = "任务标题", width = 15)
	@ApiModelProperty(value = "任务标题")
	private String taskTitle;
	/**奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券*/
	@Excel(name = "奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券", width = 15)
	@ApiModelProperty(value = "奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券")
	private String awardType;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
	@ApiModelProperty(value = "任务描述")
	private String taskDescription;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
	@ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**
	 * 券id
	 */
	private String ticketId;
}
