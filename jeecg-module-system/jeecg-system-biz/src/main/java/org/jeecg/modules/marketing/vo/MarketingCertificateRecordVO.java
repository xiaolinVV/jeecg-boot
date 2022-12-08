package org.jeecg.modules.marketing.vo;

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
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_certificate_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_certificate_record对象", description="兑换券记录")
public class MarketingCertificateRecordVO {
    
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
	/**兑换券id*/
	private String marketingCertificateId;
	/**会员列表id*/
	private String memberListId;
	/**核销店铺id，没有核销为空*/
	private String sysUserId;
	/**兑换券名称*/
	private String name;
	/**发行人；0：平台；1：店铺*/
	private String publisher;
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
	/**获取渠道*/
	private String theChannel;
	/**核销时间*/
	private Date userTime;
	/**核销人；0：平台；1：店铺*/
	private String verificationPeople;
	/**0:购买使用；1：核销使用*/
	private String verification;
	/**
	 * 核销奖励
	 */
	private String theReward;
	/********新增代码***********/
	/**会员电话号码*/
	private String phone;
	/**会员昵称*/
	private String nickName;
	/**优惠内容*/
	private String discountExplain;
	/**有效期*/
	private String startEndTime;
	/**满减多少钱*/
	private String subtract;
	/**商品个数*/
	private String mdgCount;
	/**判断是否是兑换券： 0. 优惠券 1.兑换券*/
	private String isDiscount;
	/**
	 *会员头像
	 */
	private String headPortrait;
	/**
	 *兑换商品
	 */
	private String goods;
	/**
	 *核销门店
	 */
	private String stores;
	/**
	 *状态
	 */
	private String statusName;
	/**
	 *有效期
	 */
	private String indate;
	/**
	 *获赠人
	 */
	private String giveName;
	/**
	 *使用时间
	 */
	private String ctime;

	/**
	 *核销人
	 */
	private String delName;
	/**
	 * 领取开始时间
	 */
	private String createTime_begin;
	/**
	 * 领取结束时间
	 */
	private String createTime_end;

	/**
	 * 使用开始时间
	 */
	private String userTime_begin;
	/**
	 * 使用结束时间
	 */
	private String userTime_end;
	/**
	 * 赠送开始时间
	 */
	private String useTime_begin1;
	/**
	 * 赠送结束时间
	 */
	private String useTime_end1;
	/**
	 * 店铺userId
	 */
	private String StoreUId;

	private String useTime;
	/**送出时间*/
	@Excel(name = "送出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "送出时间")
	private Date sendTime;
	/**主图*/
	@Excel(name = "主图", width = 15)
	@ApiModelProperty(value = "主图")
	private String  mainPicture;
	/**核销门店: 0: 全平台 1:指定门店*/
	@Excel(name = "核销门店: 0: 全平台 1:指定门店", width = 15)
	@ApiModelProperty(value = "核销门店: 0: 全平台 1:指定门店")
	private String  rewardStore;
	/**券类型: 0:活动券 1:付费券*/
	@Excel(name = "券类型: 0:活动券 1:付费券", width = 15)
	@ApiModelProperty(value = "券类型: 0:活动券 1:付费券")
	private String  isNomal;
	/**市场价*/
	@Excel(name = "市场价", width = 15)
	@ApiModelProperty(value = "市场价")
	private BigDecimal marketPrice;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
	@ApiModelProperty(value = "销售价")
	private BigDecimal  price;
	/**成本价*/
	@Excel(name = "成本价", width = 15)
	@ApiModelProperty(value = "成本价")
	private BigDecimal  costPrice;
	/**仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）*/
	@Excel(name = "仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）", width = 15)
	@ApiModelProperty(value = "仅销售店铺可核销（有记录到销售店铺则该券仅销售店铺可核销）")
	private String  sellRewardStore;
	/**同一家店同一天仅能核销1次*/
	@Excel(name = "同一家店同一天仅能核销1次", width = 15)
	@ApiModelProperty(value = "同一家店同一天仅能核销1次")
	private String  rewardDayOne;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
	@ApiModelProperty(value = "封面图")
	private String  coverPlan;
	/**
	 * 二维码地址
	 */
	@Excel(name = "二维码地址", width = 15)
	@ApiModelProperty(value = "二维码地址")
	private String qrAddr;

	/**海报图*/
	@Excel(name = "海报图", width = 15)
	@ApiModelProperty(value = "海报图")
	private String posters;
	//店铺名称
	private String storeName;
	/**
	 * 兑换方式；0：全部兑换；1：任选一个
	 */
	private String certificateType;
}
