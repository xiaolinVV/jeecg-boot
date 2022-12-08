package org.jeecg.modules.member.vo;

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
 * @Description: 会员分销明细
 * @Author: jeecg-boot
 * @Date:   2020-01-07
 * @Version: V1.0
 */
@Data
@TableName("member_distribution_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_distribution_record对象", description="会员分销明细")
public class MemberDistributionRecordVO {
    
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
	/**会员余额明细*/
	@Excel(name = "会员余额明细", width = 15)
    @ApiModelProperty(value = "会员余额明细")
	private String memberRechargeRecordId;
	/**商品图片*/
	@Excel(name = "商品图片", width = 15)
    @ApiModelProperty(value = "商品图片")
	private String goodPicture;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**商品规格*/
	@Excel(name = "商品规格", width = 15)
    @ApiModelProperty(value = "商品规格")
	private String goodSpecification;
	/**佣金*/
	@Excel(name = "佣金", width = 15)
    @ApiModelProperty(value = "佣金")
	private java.math.BigDecimal commission;
	/**
	 *成员头像
	 */
	private String headPortrait;
	/**
	 *成员手机号
	 */
	private String phone;
	/**
	 *成员昵称
	 */
	private String nickName;
	/**
	 *会员类型
	 */
	private String memberType;
	/**
	 *成员级别
	 */
	private String memberLevel;
	/**
	 *成员级别名称
	 */
	private String levelName;
	/**
	 *订单编号
	 */
	private String orderNo;
	/**
	 *订单状态
	 */
	private String tradeStatus;
}
