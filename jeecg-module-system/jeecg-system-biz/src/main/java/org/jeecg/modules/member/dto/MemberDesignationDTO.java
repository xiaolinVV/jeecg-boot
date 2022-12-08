package org.jeecg.modules.member.dto;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 称号管理
 * @Author: jeecg-boot
 * @Date:   2020-06-16
 * @Version: V1.0
 */
@Data
@TableName("member_designation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_designation对象", description="称号管理")
public class MemberDesignationDTO {
    
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
	/**称号名称*/
	@Excel(name = "称号名称", width = 15)
    @ApiModelProperty(value = "称号名称")
	private String name;
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String logoAddr;
	/**直推人数*/
	@Excel(name = "直推人数", width = 15)
    @ApiModelProperty(value = "直推人数")
	private BigDecimal directReferrals;
	/**总推荐人数*/
	@Excel(name = "总推荐人数", width = 15)
    @ApiModelProperty(value = "总推荐人数")
	private BigDecimal totalRecommend;
	/**分红资金占比*/
	@Excel(name = "分红资金占比", width = 15)
    @ApiModelProperty(value = "分红资金占比")
	private BigDecimal participation;

	/**状态: 0:停用; 1:启用*/
	@Excel(name = "状态: 0:停用; 1:启用", width = 15)
    @ApiModelProperty(value = "状态: 0:停用; 1:启用")
	private String status;
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
	/**删除说明*/
	@Excel(name = "删除说明", width = 15)
    @ApiModelProperty(value = "删除说明")
	private String delExplain;
	/**
	 *余额
	 */
	@Excel(name = "余额", width = 15)
	@ApiModelProperty(value = "余额")
	private BigDecimal balance;
	/**
	 *累计入账资金
	 */
	@Excel(name = "累计入账资金", width = 15)
	@ApiModelProperty(value = "累计入账资金")
	private BigDecimal recordedMoney;
	/**
	 *累计出账资金
	 */
	@Excel(name = "累计出账资金", width = 15)
	@ApiModelProperty(value = "累计出账资金")
	private BigDecimal enterMoney;
	/**
	 *级别，数字越小等级越低
	 */
	@Excel(name = "级别，数字越小等级越低", width = 15)
	@ApiModelProperty(value = "级别，数字越小等级越低")
	private BigDecimal sort;

	@Excel(name = "被推荐人类型；0：普通会员和vip会员；1：仅vip会员；2：仅买礼包成为vip的会员", width = 15)
	@ApiModelProperty(value = "被推荐人类型；0：普通会员和vip会员；1：仅vip会员；2：仅买礼包成为vip的会员")
	private String recommendedType;

	@Excel(name = "是否均分；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否均分；0：否；1：是")
	private String isAverage;

	@Excel(name = "是否参与低级分红；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否参与低级分红；0：否；1：是")
	private String lowLevelDividends;

	@Excel(name = "低级称号id；多用逗号隔开", width = 15)
	@ApiModelProperty(value = "低级称号id；多用逗号隔开")
	private String memberDesignations;

	@Excel(name = "是否开启月绩效；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否开启月绩效；0：否；1：是")
	private String isMonthlyPerformance;

	@Excel(name = "累计新增推广人数", width = 15)
	@ApiModelProperty(value = "累计新增推广人数")
	private BigDecimal totalPeoples;

	@Excel(name = "累计消费额", width = 15)
	@ApiModelProperty(value = "累计消费额")
	private BigDecimal totalPrices;

	@Excel(name = "自定义特权说明", width = 15)
	@ApiModelProperty(value = "自定义特权说明")
	private String customRemark;

	@Excel(name = "消费金额百分比", width = 15)
	@ApiModelProperty(value = "消费金额百分比")
	private BigDecimal totalPercent;

	@Excel(name = "关联直推的下次称号id", width = 15)
	@ApiModelProperty(value = "关联直推的下次称号id")
	private String straightPushId;

	@Excel(name = "关联直推人数", width = 15)
	@ApiModelProperty(value = "关联直推人数")
	private BigDecimal straightPushProple;

	@Excel(name = "团队人数", width = 15)
	@ApiModelProperty(value = "团队人数")
	private BigDecimal totalTeams;

	@Excel(name = "是否开启资金分配；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否开启资金分配；0：否；1：是")
	private String isOpenMoney;

	/**
	 * 升级条件
	 */
	private String upgradeCondition;

	private String createTime_begin;
	private String createTime_end;
	private String updateTime_begin;
	private String updateTime_end;
	/**
	 * 称号id
	 */
	private String memberDesignationId;
	@Excel(name = "礼包id", width = 15)
	@ApiModelProperty(value = "礼包id")
	private String marketingGiftBagId;

	@Excel(name = "团队礼包销售总额", width = 15)
	@ApiModelProperty(value = "团队礼包销售总额")
	private BigDecimal giftTotalSales;
	@Excel(name = "称号团队id", width = 15)
	@ApiModelProperty(value = "称号团队id")
	private String memberDesignationGroupId;

	//礼包名称
	private String giftName;

	/**
	 * 称号团队名称
	 */
	private String groupName;
	@Excel(name = "默认称号: 0: 为否 1:为是", width = 15)
	@ApiModelProperty(value = "默认称号: 0: 为否 1:为是")
	private String isDefault;
	@Excel(name = "是否以购买礼包作为获得当前称号的获取条件：0为否；1为是；只有当前值为1时，礼包中赠送称号时方可配置当前称号", width = 15)
	@ApiModelProperty(value = "是否以购买礼包作为获得当前称号的获取条件：0为否；1为是；只有当前值为1时，礼包中赠送称号时方可配置当前称号")
	private String isBuyGiftbag;
}
