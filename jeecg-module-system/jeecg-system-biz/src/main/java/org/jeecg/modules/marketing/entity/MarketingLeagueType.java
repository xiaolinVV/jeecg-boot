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
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 加盟专区-专区类型
 * @Author: jeecg-boot
 * @Date:   2021-12-21
 * @Version: V1.0
 */
@Data
@TableName("marketing_league_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_league_type对象", description="加盟专区-专区类型")
public class MarketingLeagueType {
    
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
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
	private String typeName;
	/**活动价格*/
	@Excel(name = "活动价格", width = 15)
    @ApiModelProperty(value = "活动价格")
	private BigDecimal price;
	/**直推奖励*/
	@Excel(name = "直推奖励", width = 15)
    @ApiModelProperty(value = "直推奖励")
	private BigDecimal pushStraightReward;
	/**店长奖励*/
	@Excel(name = "店长奖励", width = 15)
    @ApiModelProperty(value = "店长奖励")
	private BigDecimal managerReward;
	/**店长管理奖励*/
	@Excel(name = "店长管理奖励", width = 15)
    @ApiModelProperty(value = "店长管理奖励")
	private BigDecimal storeManagerReward;
	/**店长扶植奖励*/
	@Excel(name = "店长扶植奖励", width = 15)
    @ApiModelProperty(value = "店长扶植奖励")
	private BigDecimal storeManagerSupportAward;
	/**超级合伙人管理奖励*/
	@Excel(name = "超级合伙人管理奖励", width = 15)
    @ApiModelProperty(value = "超级合伙人管理奖励")
	private BigDecimal superPartnerAward;
	/**合伙人管理奖励*/
	@Excel(name = "合伙人管理奖励", width = 15)
    @ApiModelProperty(value = "合伙人管理奖励")
	private BigDecimal partnerStoreReward;
	/**是否复购；0：否；1：是*/
	@Excel(name = "是否复购；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否复购；0：否；1：是")
	private String afterPurchase;
	/**复购直推奖励比例*/
	@Excel(name = "复购直推奖励比例", width = 15)
    @ApiModelProperty(value = "复购直推奖励比例")
	private BigDecimal afterPushStraightReward;
	/**复购间推奖励比例*/
	@Excel(name = "复购间推奖励比例", width = 15)
    @ApiModelProperty(value = "复购间推奖励比例")
	private BigDecimal betweenPush;
	/**积分数量*/
	@Excel(name = "积分数量", width = 15)
    @ApiModelProperty(value = "积分数量")
	private BigDecimal welfarePayments;
	/**级别*/
	@Excel(name = "级别", width = 15)
    @ApiModelProperty(value = "级别")
	private BigDecimal grade;

	private String status;

	private BigDecimal sort;

	private String statusExplain;

	/*城市服务商*/
	private BigDecimal cityServiceProvider;
}
