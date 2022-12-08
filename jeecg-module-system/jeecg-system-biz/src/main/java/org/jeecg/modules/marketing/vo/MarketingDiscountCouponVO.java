package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-12-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount_coupon")
public class MarketingDiscountCouponVO {
    
	/**主键ID*/
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	private Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	private Date updateTime;
	/**创建年*/
	private Integer year;
	/**创建月*/
	private Integer month;
	/**创建日*/
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;
	/**优惠金额*/
	private java.math.BigDecimal price;
	/**优惠券名称*/
	private String name;
	/**有无门槛；0：无；1：有*/
	private String isThreshold;
	/**会员id*/
	private String memberListId;
	/**店铺id*/
	private String sysUserId;
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;
	/*优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；*/
	@Excel(name = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；", width = 15,dicCode = "vouchers_status")
	@ApiModelProperty(value = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；")
	@Dict(dicCode = "vouchers_status")
	private String status;
	/**券号*/
	private String qqzixuangu;
	/**领取渠道名称*/
	private String theChannel;
	/**平台渠道id*/
	private String marketingChannelId;
	/**使用时间*/
	private Date userTime;
	/**使用时间(前端展示)*/
	private String userTimes;
	/**实际抵扣金额（核销默认全抵）*/
	private java.math.BigDecimal practicalDeduction;
	/**优惠券的id*/
	private String marketingDiscountId;
	/**0:店铺；1：平台*/
	private String isPlatform;
	/**满多少钱*/
	private java.math.BigDecimal completely;

	private String giveMemberListId;
	/**0:购买使用；1：核销使用*/
	private String verification;

	/********新增代码***********/
	/**
	 * 会员头像
	 */
	private String headPortrait;
	/**会员电话号码*/
	private String phone;
	/**会员昵称*/
	private String nickName;
	/**优惠内容*/
	private String discountExplain;
	/**判断是否是兑换券： 0. 优惠券 1.兑换券*/
	private String isDiscount;
	/**
	 *使用门槛
	 */
	private String usingThreshold;
	/**
	 * 优惠内容
	 */
	private String preferentialContent;
	/**
	 *适用商品
	 */
	private String applyGood;
	/**
	 *有效期
	 */
	private String indate;
	/**
	 *赠送时间
	 */
	private String giveTime;
	/**
	 * 状态
	 */
	private String statusName;
	/**
	 * 赠送人
	 */
	private String giveName;
	/**再次领取条件；0：已送出；1：已使用；已过期；逗号隔开*/
	@Excel(name = "再次领取条件；0：已送出；1：已使用；已过期；逗号隔开", width = 15,dicCode = "again_get")
	@ApiModelProperty(value = "再次领取条件；0：已送出；1：已使用；已过期；逗号隔开")
	@Dict(dicCode = "again_get")
	private String againGet;

	private String goodSkr;

	/**
	 * 门店名称
	 */
	private String storeName;

	private String Uid;

	/**送出时间*/
	@Excel(name = "送出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "送出时间")
	private Date sendTime;
	/**是否唯一；0：否；1：是*/
	@Excel(name = "是否唯一；0：否；1：是", width = 15)
	@ApiModelProperty(value = "是否唯一；0：否；1：是")
	private String isUniqueness;

	/**
	 *领取时间开始
	 */
	private String createTime_begin;
	/**
	 *领取时间结束
	 */
	private String createTime_end;
	/**
	 *使用时间开始
	 */
	private String userTime_begin;
	/**
	 *使用时间结束
	 */
	private String userTime_end;
	/**
	 *赠送时间开始
	 */
	private String sendTime_begin;
	/**
	 *赠送时间结束
	 */
	private String sendTime_end;

	private String niName;

	private String issuer;
}
