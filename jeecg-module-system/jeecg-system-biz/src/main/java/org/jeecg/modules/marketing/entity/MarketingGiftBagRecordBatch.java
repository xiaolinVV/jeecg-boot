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
import java.util.Date;

/**
 * @Description: 采购礼包记录
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Data
@TableName("marketing_gift_bag_record_batch")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_gift_bag_record_batch对象", description="采购礼包记录")
public class MarketingGiftBagRecordBatch {
    
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
	/**礼包名称*/
	@Excel(name = "礼包名称", width = 15)
    @ApiModelProperty(value = "礼包名称")
	private String giftName;
	/**礼包价格*/
	@Excel(name = "礼包价格", width = 15)
    @ApiModelProperty(value = "礼包价格")
	private BigDecimal price;
	/**发货次数*/
	@Excel(name = "发货次数", width = 15)
    @ApiModelProperty(value = "发货次数")
	private BigDecimal sendTimes;
	/**省代*/
	@Excel(name = "省代", width = 15)
    @ApiModelProperty(value = "省代")
	private String proviceId;
	/**市代*/
	@Excel(name = "市代", width = 15)
    @ApiModelProperty(value = "市代")
	private String cityId;
	/**区县代*/
	@Excel(name = "区县代", width = 15)
    @ApiModelProperty(value = "区县代")
	private String towmId;
	/**购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type*/
	@Excel(name = "购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type", width = 15)
    @ApiModelProperty(value = "购买人限制：会员类型：0：普通会员；1：vip会员 字典：member_type")
	private String buyLimit;
	/**限制购买的会员等级，等级id逗号隔开*/
	@Excel(name = "限制购买的会员等级，等级id逗号隔开", width = 15)
    @ApiModelProperty(value = "限制购买的会员等级，等级id逗号隔开")
	private String buyVipMemberGradeId;
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private BigDecimal buyCount;
	/**礼包可见范围；0：指定门店；1：全平台*/
	@Excel(name = "礼包可见范围；0：指定门店；1：全平台", width = 15)
    @ApiModelProperty(value = "礼包可见范围；0：指定门店；1：全平台")
	private String viewScope;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private Date endTime;
	/**主图；逗号隔开*/
	@Excel(name = "主图；逗号隔开", width = 15)
    @ApiModelProperty(value = "主图；逗号隔开")
	private String mainPicture;
	/**详情图*/
	@Excel(name = "详情图", width = 15)
    @ApiModelProperty(value = "详情图")
	private String giftDeals;
	/**分享图*/
	@Excel(name = "分享图", width = 15)
    @ApiModelProperty(value = "分享图")
	private String coverPlan;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**礼包说明*/
	@Excel(name = "礼包说明", width = 15)
    @ApiModelProperty(value = "礼包说明")
	private String giftExplain;
	/**采购礼包id*/
	@Excel(name = "采购礼包id", width = 15)
    @ApiModelProperty(value = "采购礼包id")
	private String marketingGiftBagBatchId;
	/**礼包编号*/
	@Excel(name = "礼包编号", width = 15)
    @ApiModelProperty(value = "礼包编号")
	private String giftNo;
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**应付款（支付前标准金额）*/
	@Excel(name = "应付款（支付前标准金额）", width = 15)
    @ApiModelProperty(value = "应付款（支付前标准金额）")
	private BigDecimal customaryDues;
	/**实付款（支付后标准金额）*/
	@Excel(name = "实付款（支付后标准金额）", width = 15)
    @ApiModelProperty(value = "实付款（支付后标准金额）")
	private BigDecimal actualPayment;
	/**支付状态;0:未支付；1：已支付*/
	@Excel(name = "支付状态;0:未支付；1：已支付", width = 15)
    @ApiModelProperty(value = "支付状态;0:未支付；1：已支付")
	private String payStatus;
	/**购买时间*/
	@Excel(name = "购买时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "购买时间")
	private Date payTime;
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
	/**推广人类型;0:店铺；1：会员；2：平台*/
	@Excel(name = "推广人类型;0:店铺；1：会员；2：平台", width = 15)
    @ApiModelProperty(value = "推广人类型;0:店铺；1：会员；2：平台")
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
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
	private BigDecimal longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
	private BigDecimal latitude;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
	private String linkman;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phone;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private String areaAddress;
	/**区域描述，逗号隔开*/
	@Excel(name = "区域描述，逗号隔开", width = 15)
    @ApiModelProperty(value = "区域描述，逗号隔开")
	private String areaExplan;
	/**二级推广人*/
	@Excel(name = "二级推广人", width = 15)
    @ApiModelProperty(value = "二级推广人")
	private String promoterTwo;
	/**二级推广人类型;0:店铺；1：会员；2：平台*/
	@Excel(name = "二级推广人类型;0:店铺；1：会员；2：平台", width = 15)
    @ApiModelProperty(value = "二级推广人类型;0:店铺；1：会员；2：平台")
	private String promoterTypeTwo;
	/**加盟商id*/
	@Excel(name = "加盟商id", width = 15)
    @ApiModelProperty(value = "加盟商id")
	private String allianceId;
	/**收款说明*/
	@Excel(name = "收款说明", width = 15)
    @ApiModelProperty(value = "收款说明")
	private String gatheringExplain;
	/**收款凭证*/
	@Excel(name = "收款凭证", width = 15)
    @ApiModelProperty(value = "收款凭证")
	private String gatheringProof;
	/**收款凭证*/
	@Excel(name = "留言", width = 15)
	@ApiModelProperty(value = "留言")
	private String message;
	/**收款凭证*/
	@Excel(name = "配送金额", width = 15)
	@ApiModelProperty(value = "配送金额")
	private BigDecimal shipFee;
}
