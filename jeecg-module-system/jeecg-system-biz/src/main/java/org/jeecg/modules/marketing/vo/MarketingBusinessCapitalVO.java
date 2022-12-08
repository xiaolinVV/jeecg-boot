package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 创业资金池配置
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_business_capital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_business_capital对象", description="创业资金池配置")
public class MarketingBusinessCapitalVO extends JeecgEntity {
    
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
	/**资金池类型；0：非称号；1：称号*/
	@Excel(name = "资金池类型；0：非称号；1：称号", width = 15)
    @ApiModelProperty(value = "资金池类型；0：非称号；1：称号")
	private String capitalType;
	/**资金池名称*/
	@Excel(name = "资金池名称", width = 15)
    @ApiModelProperty(value = "资金池名称")
	private String capitalName;
	/**创业团队称号id（没有就为空）*/
	@Excel(name = "创业团队称号id（没有就为空）", width = 15)
    @ApiModelProperty(value = "创业团队称号id（没有就为空）")
	private String marketingBusinessDesignationId;
	/**进账比例*/
	@Excel(name = "进账比例", width = 15)
    @ApiModelProperty(value = "进账比例")
	private java.math.BigDecimal makeProportion;
	/**进账控制；0：关闭；1开启*/
	@Excel(name = "进账控制；0：关闭；1开启", width = 15)
    @ApiModelProperty(value = "进账控制；0：关闭；1开启")
	private String sessionControl;
	/**星期，逗号分隔开来*/
	@Excel(name = "星期，逗号分隔开来", width = 15)
    @ApiModelProperty(value = "星期，逗号分隔开来")
	private String weeks;
	/**出账方式；0：无；1：每日分红；2：手动出账*/
	@Excel(name = "出账方式；0：无；1：每日分红；2：手动出账", width = 15)
    @ApiModelProperty(value = "出账方式；0：无；1：每日分红；2：手动出账")
	private String paymentsModel;
	/**是否分红；0：无；1：启用*/
	@Excel(name = "是否分红；0：无；1：启用", width = 15)
    @ApiModelProperty(value = "是否分红；0：无；1：启用")
	private String whetherDividend;
	/**分红金额*/
	@Excel(name = "分红金额", width = 15)
    @ApiModelProperty(value = "分红金额")
	private java.math.BigDecimal amountShare;
	/**是否复投金额；0：无；1：启用*/
	@Excel(name = "是否复投金额；0：无；1：启用", width = 15)
    @ApiModelProperty(value = "是否复投金额；0：无；1：启用")
	private String whetherComplex;
	/**复投金额*/
	@Excel(name = "复投金额", width = 15)
    @ApiModelProperty(value = "复投金额")
	private java.math.BigDecimal investmentAmount;
	/**是否显示；0：否；1：是*/
	@Excel(name = "是否显示；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否显示；0：否；1：是")
	private Boolean isView;
	/**资金池余额*/
	@Excel(name = "资金池余额", width = 15)
    @ApiModelProperty(value = "资金池余额")
	private java.math.BigDecimal balance;
	/**入账金额*/
	@Excel(name = "入账金额", width = 15)
    @ApiModelProperty(value = "入账金额")
	private java.math.BigDecimal recordedAmount;
	/**出账金额*/
	@Excel(name = "出账金额", width = 15)
    @ApiModelProperty(value = "出账金额")
	private java.math.BigDecimal accountsAmount;
	/**控制说明*/
	@Excel(name = "控制说明", width = 15)
    @ApiModelProperty(value = "控制说明")
	private String sessionControlExplain;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
    @ApiModelProperty(value = "乐观锁")
	private Integer version;
}
