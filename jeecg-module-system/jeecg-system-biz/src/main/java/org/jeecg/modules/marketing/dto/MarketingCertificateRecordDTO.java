package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.marketing.entity.MarketingGiftBagCertificate;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class MarketingCertificateRecordDTO {
    
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
	 * 兑换方式；0：全部兑换；1：任选一个
	 */
	private String certificateType;
	/**
	 * 查询领取时间开始
	 */
	private String createTime_begin;
	/**
	 * 查询领取时间结束
	 */
	private String createTime_end;

	/**
	 * 数量
	 */
	private BigDecimal quantity;

	private List<MarketingGiftBagCertificate> marketingGiftBagCertificateList;
}
