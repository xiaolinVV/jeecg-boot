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
 * @Description: 拼购管理
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
@Data
@TableName("marketing_group_integral_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_group_integral_manage对象", description="拼购管理")
public class MarketingGroupIntegralManage {
    
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
	/**活动别名*/
	@Excel(name = "活动别名", width = 15)
    @ApiModelProperty(value = "活动别名")
	private String anotherName;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
	private String surfacePlot;
	/**主图*/
	@Excel(name = "主图", width = 15)
    @ApiModelProperty(value = "主图")
	private String mainPicture;
	/**支付方式；0：产品券*/
	@Excel(name = "支付方式；0：产品券", width = 15)
    @ApiModelProperty(value = "支付方式；0：产品券")
	private String payment;
	/**支付金额*/
	@Excel(name = "支付金额", width = 15)
    @ApiModelProperty(value = "支付金额")
	private java.math.BigDecimal paymentAmount;
	/**成团人数*/
	@Excel(name = "成团人数", width = 15)
    @ApiModelProperty(value = "成团人数")
	private java.math.BigDecimal numberClusters;
	/**参与拼购奖励*/
	@Excel(name = "参与拼购奖励", width = 15)
    @ApiModelProperty(value = "参与拼购奖励")
	private java.math.BigDecimal groupRewards;
	/**推荐拼购奖励*/
	@Excel(name = "推荐拼购奖励", width = 15)
    @ApiModelProperty(value = "推荐拼购奖励")
	private java.math.BigDecimal recommendGroupRewards;
	/**中奖奖励类型；0：兑换券*/
	@Excel(name = "中奖奖励类型；0：兑换券", width = 15)
    @ApiModelProperty(value = "中奖奖励类型；0：兑换券")
	private String prizeType;
	/**中奖奖励数量*/
	@Excel(name = "中奖奖励数量", width = 15)
    @ApiModelProperty(value = "中奖奖励数量")
	private java.math.BigDecimal winningNumber;
	/**未中奖奖品类型0：产品券*/
	@Excel(name = "未中奖奖品类型0：产品券", width = 15)
    @ApiModelProperty(value = "未中奖奖品类型0：产品券")
	private String rewardType;
	/**未中奖励数量*/
	@Excel(name = "未中奖励数量", width = 15)
    @ApiModelProperty(value = "未中奖励数量")
	private java.math.BigDecimal missedRewards;
	/**转化阈值*/
	@Excel(name = "转化阈值", width = 15)
    @ApiModelProperty(value = "转化阈值")
	private java.math.BigDecimal threshold;
	/**转入余额*/
	@Excel(name = "转入余额", width = 15)
    @ApiModelProperty(value = "转入余额")
	private java.math.BigDecimal intoBalance;
	/**转入购物积分*/
	@Excel(name = "转入购物积分", width = 15)
    @ApiModelProperty(value = "转入购物积分")
	private java.math.BigDecimal intoShoppingCredits;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.math.BigDecimal sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
}
