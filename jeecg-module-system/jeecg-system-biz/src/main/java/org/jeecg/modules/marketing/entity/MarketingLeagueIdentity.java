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

import java.math.BigDecimal;

/**
 * @Description: 身份设置
 * @Author: jeecg-boot
 * @Date:   2021-12-17
 * @Version: V1.0
 */
@Data
@TableName("marketing_league_identity")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_league_identity对象", description="身份设置")
public class MarketingLeagueIdentity {
    
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
	/**身份名称*/
	@Excel(name = "身份名称", width = 15)
    @ApiModelProperty(value = "身份名称")
	private String identityName;
	/**是否默认；（0：否；1：是）*/
	@Excel(name = "是否默认；（0：否；1：是）", width = 15)
    @ApiModelProperty(value = "是否默认；（0：否；1：是）")
	private String isDefault;
	/**获得方式；（0：注册；1：加盟区下单；2：晋升；3：缴费；4：后台开通）*/
	@Excel(name = "获得方式；（0：注册；1：加盟区下单；2：晋升；3：缴费；4：后台开通）", width = 15)
    @ApiModelProperty(value = "获得方式；（0：注册；1：加盟区下单；2：晋升；3：缴费；4：后台开通）")
	private String getWay;
	/**前置身份id*/
	@Excel(name = "前置身份id", width = 15)
    @ApiModelProperty(value = "前置身份id")
	private String frontMarketingLeagueIdentityId;
	/**后置身份id*/
	@Excel(name = "后置身份id", width = 15)
    @ApiModelProperty(value = "后置身份id")
	private String afterMarketingLeagueIdentityId;
	/**缴交金额*/
	@Excel(name = "缴交金额", width = 15)
    @ApiModelProperty(value = "缴交金额")
	private BigDecimal payPrice;
	/**是否直推奖励；0：否；1：是*/
	@Excel(name = "是否直推奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否直推奖励；0：否；1：是")
	private String pushStraightReward;
	/**是否店长奖励；0：否；1：是*/
	@Excel(name = "是否店长奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否店长奖励；0：否；1：是")
	private String managerReward;
	/**是否店长管理奖励；0：否；1：是*/
	@Excel(name = "是否店长管理奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否店长管理奖励；0：否；1：是")
	private String storeManagerReward;
	/**城市服务商奖励；0：否；1：是*/
	@Excel(name = "城市服务商奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "城市服务商奖励；0：否；1：是")
	private String cityServiceProviderAward;
	/**超级合伙人奖励；0：否；1：是*/
	@Excel(name = "超级合伙人奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "超级合伙人奖励；0：否；1：是")
	private String superPartnerAward;
	/**店长扶持奖励；0：否；1：是*/
	@Excel(name = "店长扶持奖励；0：否；1：是", width = 15)
    @ApiModelProperty(value = "店长扶持奖励；0：否；1：是")
	private String storeManagerSupportAward;
	/**是否附加身份；0：否；1：是*/
	@Excel(name = "是否附加身份；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否附加身份；0：否；1：是")
	private String additionalIdentity;
	/**是否支持附加身份；0：否；1：是*/
	@Excel(name = "是否支持附加身份；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否支持附加身份；0：否；1：是")
	private String supportAdditionalIdentity;
	/*宣传海报*/
	private String poster;
	/*直推*/
	private BigDecimal directDrive;

	/*合伙人店长奖励*/
	private String partnerStoreReward;
}
