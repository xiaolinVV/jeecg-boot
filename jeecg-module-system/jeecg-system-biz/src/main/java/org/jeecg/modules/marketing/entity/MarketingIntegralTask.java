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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 积分任务
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Data
@TableName("marketing_integral_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_integral_task对象", description="积分任务")
public class MarketingIntegralTask {
    
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
	/**任务编号*/
	@Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
	private String serialNumber;
	/**任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告*/
	@Excel(name = "任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告", width = 15,dicCode = "task_type")
	@ApiModelProperty(value = "任务类型: 0:用户注册 1:交易密码 2: 连续签到 3:每日浏览 4: 邀请签到 5; 邀请注册 6:身份认证 7: 每日签到 8: 好友首单 9: 观看广告")
	@Dict(dicCode = "task_type")
	private String taskType;
	/**任务标题*/
	@Excel(name = "任务标题", width = 15)
    @ApiModelProperty(value = "任务标题")
	private String taskTitle;
	/**任务方式:0 每日多次 1: 每日单次 2:唯一任务*/
	@Excel(name = "任务方式:0 每日多次 1: 每日单次 2:唯一任务", width = 15,dicCode = "task_way")
    @ApiModelProperty(value = "任务方式:0 每日多次 1: 每日单次 2:唯一任务")
	@Dict(dicCode = "task_way")
	private String taskeMode;
	/**显示位置；0：签到栏；1：任务栏；2：奖励栏*/
	@Excel(name = "显示位置；0：签到栏；1：任务栏；2：奖励栏", width = 15)
    @ApiModelProperty(value = "显示位置；0：签到栏；1：任务栏；2：奖励栏")
	private String displayPosition;
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String taskImg;
	/**奖励*/
	@Excel(name = "奖励", width = 15)
    @ApiModelProperty(value = "奖励")
	private BigDecimal award;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
	/**奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券*/
	@Excel(name = "奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券", width = 15)
    @ApiModelProperty(value = "奖励类型；0：积分(水滴)；1；专区团参团次数；2：平台优惠券；3：店铺优惠券")
	private String awardType;
	/**
	 * 签到天数
	 */
	@Excel(name = "签到天数", width = 15)
	@ApiModelProperty(value = "签到天数")
	private BigDecimal numberDays;

	/**
	 * 任务描述
	 */
	@Excel(name = "任务描述", width = 15)
	@ApiModelProperty(value = "任务描述")
	private String taskDescription;
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
	@ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**
	 * 分享图
	 */
	private String coverPlan;

	/**
	 * 是否显示；0：不显示；1：显示
	 */
	private String isView;

	/**
	 * 排序
	 */
	private BigDecimal sort;
	/**
	 * 任务次数
	 */
	private BigDecimal times;
	/**
	 * 券id
	 */
	private String ticketId;
}
