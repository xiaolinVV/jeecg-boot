package org.jeecg.modules.marketing.vo;

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
import java.util.Date;

/**
 * @Description: 礼包记录
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Data
@TableName("marketing_gift_bag_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_gift_bag_record对象", description="礼包记录")
public class MarketingGiftBagRecordVO {

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
	@TableLogic
	private String delFlag;
	/**礼包id*/
	@Excel(name = "礼包id", width = 15)
    @ApiModelProperty(value = "礼包id")
	private String marketingGiftBagId;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**礼包编号*/
	@Excel(name = "礼包编号", width = 15)
    @ApiModelProperty(value = "礼包编号")
	private String giftNo;
	/**礼包名称*/
	@Excel(name = "礼包名称", width = 15)
    @ApiModelProperty(value = "礼包名称")
	private String giftName;
	/**礼包价格*/
	@Excel(name = "礼包价格", width = 15)
    @ApiModelProperty(value = "礼包价格")
	private BigDecimal price;
	/**购买时间*/
	@Excel(name = "购买时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "购买时间")
	private Date payTime;
	/**支付状态;0:未支付；1：已支付*/
	@Excel(name = "支付状态;0:未支付；1：已支付", width = 15)
    @ApiModelProperty(value = "支付状态;0:未支付；1：已支付")
	private String payStatus;
	/**归属店铺*/
	@Excel(name = "归属店铺", width = 15)
    @ApiModelProperty(value = "归属店铺")
	private String affiliationStore;
	/**归属渠道*/
	@Excel(name = "归属渠道", width = 15)
    @ApiModelProperty(value = "归属渠道")
	private String distributionChannel;
	/**推广人*/
	@Excel(name = "推广人", width = 15)
    @ApiModelProperty(value = "推广人")
	private String promoter;

    @Excel(name = "推广人类型", width = 15)
    @ApiModelProperty(value = "推广人类型")
    private String promoterType;
	/**回调状态；0：未回调；1：已回调*/
	@Excel(name = "回调状态；0：未回调；1：已回调", width = 15)
	@ApiModelProperty(value = "回调状态；0：未回调；1：已回调")
	private String backStatus;
	/**回调次数*/
	@Excel(name = "回调次数", width = 15)
	@ApiModelProperty(value = "回调次数")
	private BigDecimal backTimes;

	/**支付日志*/
	@Excel(name = "支付日志", width = 15)
	@ApiModelProperty(value = "支付日志")
	private String payParam;

	@Excel(name = "主图", width = 15)
	@ApiModelProperty(value = "主图")
	private String mainPicture;
	@Excel(name = "详情图", width = 15)
	@ApiModelProperty(value = "详情图")
	private String giftDeals;
	/**会员类型，按照字典,逗号隔开*/
	@Excel(name = "会员类型，按照字典,逗号隔开", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "会员类型，按照字典,逗号隔开")
	@Dict(dicCode = "member_type")
	private String buyLimit;
	/**每人限制购买次数*/
	@Excel(name = "每人限制购买次数", width = 15)
	@ApiModelProperty(value = "每人限制购买次数")
	private BigDecimal limitTimes;
	/**礼包说明*/
	@Excel(name = "礼包说明", width = 15)
	@ApiModelProperty(value = "礼包说明")
	private String giftExplain;
	/**
	 * vip特权；0：不赠送；1：赠送：
	 */
	@Excel(name = "vip特权；0：不赠送；1：赠送：", width = 15)
	@ApiModelProperty(value = "vip特权；0：不赠送；1：赠送：")
	private String vipPrivilege;
	/**
	 * 福利金
	 */
	@Excel(name = "福利金", width = 15)
	@ApiModelProperty(value = "福利金")
	private BigDecimal welfarePayments;
	/**
	 * 头像
	 */
	@Excel(name = "头像", width = 15)
	@ApiModelProperty(value = "头像")
	private String headPortrait;
	/**
	 *手机
	 */
	@Excel(name = "手机", width = 15)
	@ApiModelProperty(value = "手机")
	private String phone;
	/**
	 *昵称
	 */
	@Excel(name = "昵称", width = 15)
	@ApiModelProperty(value = "昵称")
	private String nickName;
	/**
	 *发行时间
	 */
	private String bagTime;
	/**
	 * 渠道名称
	 */
	private String channelName;
	/**
	 * 归属店铺名称
	 */
	private String storeName;
	/**
	 * 归属人
	 */
	private String promoterName;

	/**
	 *购买时间开始
	 */
	private String payTime_begin;
	/**
	 *购买时间结束
	 */
	private String payTime_end;
	/**
	 *发行时间开始
	 */
	private String startTime_begin;
	/**
	 *发行时间结束
	 */
	private String endTime_end;

	/**推广人id*/
	@Excel(name = "推广人id", width = 15)
	@ApiModelProperty(value = "推广人id")
	private String tMemberId;

	@Excel(name = "分销特权：0：不送分销特权，1：送分销特权；", width = 15)
	@ApiModelProperty(value = "分销特权：0：不送分销特权，1：送分销特权；")
	private String distributionPrivileges;

	@Excel(name = "团队特权：0：不送团队特权，1：送团队特权；", width = 15)
	@ApiModelProperty(value = "团队特权：0：不送团队特权，1：送团队特权；")
	private String teamPrivileges;
	/**
	 *  加盟商id
	 */
	private String allianceManageUserId;
	/**
	 * 礼包分享人
	 */
	private String shareName;
	/**
	 * 二级推广人
	 */
	private String promoterTwoName;
}
