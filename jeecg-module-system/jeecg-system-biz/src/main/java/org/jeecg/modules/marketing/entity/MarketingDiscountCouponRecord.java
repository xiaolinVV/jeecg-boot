package org.jeecg.modules.marketing.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: marketing_discount_coupon_record
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount_coupon_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="marketing_discount_coupon_record对象", description="marketing_discount_coupon_record")
public class MarketingDiscountCouponRecord extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
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
	/**删除原因*/
	@Excel(name = "删除原因", width = 15)
    @ApiModelProperty(value = "删除原因")
    private String delReason;
	/**会员优惠券id*/
	@Excel(name = "会员优惠券id", width = 15)
    @ApiModelProperty(value = "会员优惠券id")
    private String marketingDiscountCouponId;
	/**0:店铺；1：平台*/
	@Excel(name = "0:店铺；1：平台", width = 15)
    @ApiModelProperty(value = "0:店铺；1：平台")
    private String isPlatform;
	/**券类型；0：常规；1：活动 2：折扣*/
	@Excel(name = "券类型；0：常规；1：活动 2：折扣", width = 15)
    @ApiModelProperty(value = "券类型；0：常规；1：活动 2：折扣")
    private String isNomad;
	/**交易金额*/
	@Excel(name = "交易金额", width = 15)
    @ApiModelProperty(value = "交易金额")
    private BigDecimal amount;
	/**参与优惠商品金额*/
	@Excel(name = "参与优惠商品金额", width = 15)
    @ApiModelProperty(value = "参与优惠商品金额")
    private BigDecimal discountGoodAmount;
	/**折扣上限金额，最多可以参与折扣的金额上限（用于折扣券）*/
	@Excel(name = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券）", width = 15)
    @ApiModelProperty(value = "折扣上限金额，最多可以参与折扣的金额上限（用于折扣券）")
    private BigDecimal discountLimitAmount;
	/**优惠折扣百分比：指参与折扣的力度（用于折扣券）*/
	@Excel(name = "优惠折扣百分比：指参与折扣的力度（用于折扣券）", width = 15)
    @ApiModelProperty(value = "优惠折扣百分比：指参与折扣的力度（用于折扣券）")
    private BigDecimal discountPercent;
	/**折扣券使用金额*/
	@Excel(name = "折扣券使用金额", width = 15)
    @ApiModelProperty(value = "折扣券使用金额")
    private BigDecimal discountUseAmount;
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal coupon;
	/**折扣券可用余额*/
	@Excel(name = "折扣券可用余额", width = 15)
    @ApiModelProperty(value = "折扣券可用余额")
    private BigDecimal discountBalance;
	/**使用时间*/
	@Excel(name = "使用时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "使用时间")
    private Date userTime;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
    private String memberListId;
	/**店铺id*/
	@Excel(name = "店铺id", width = 15)
    @ApiModelProperty(value = "店铺id")
    private String storeSysUserId;
	/**平台订单id*/
	@Excel(name = "平台订单id", width = 15)
    @ApiModelProperty(value = "平台订单id")
    private String orderListId;
	/**店铺订单id*/
	@Excel(name = "店铺订单id", width = 15)
    @ApiModelProperty(value = "店铺订单id")
    private String orderStoreListId;
	/**折扣券关键参数json*/
	@Excel(name = "折扣券关键参数json", width = 15)
    @ApiModelProperty(value = "折扣券关键参数json")
    private String discountSettleJson;
}
