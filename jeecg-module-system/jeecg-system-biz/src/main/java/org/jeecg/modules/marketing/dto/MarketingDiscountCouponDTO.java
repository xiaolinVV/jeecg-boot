package org.jeecg.modules.marketing.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.marketing.entity.MarketingGiftBagDiscount;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date: 2019-12-13
 * @Version: V1.0
 */
@Data
@TableName("marketing_discount_coupon")
public class MarketingDiscountCouponDTO {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建年
     */
    private Integer year;
    /**
     * 创建月
     */
    private Integer month;
    /**
     * 创建日
     */
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 优惠金额
     */
    private BigDecimal price;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 有无门槛；0：无；1：有
     */
    private String isThreshold;
    /**
     * 会员id
     */
    private String memberListId;
    /**
     * 店铺id
     */
    private String sysUserId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /*优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；*/
    @Excel(name = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；", width = 15, dicCode = "vouchers_status")
    @ApiModelProperty(value = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；")
    @Dict(dicCode = "vouchers_status")
    private String status;
    /**
     * 券号
     */
    private String qqzixuangu;
    /**
     * 领取渠道名称
     */
    private String theChannel;
    /**
     * 平台渠道id
     */
    private String marketingChannelId;
    /**
     * 使用时间
     */
    private Date userTime;
    /**
     * 实际抵扣金额（核销默认全抵）
     */
    private BigDecimal practicalDeduction;
    /**
     * 优惠券的id
     */
    private String marketingDiscountId;
    /**
     * 0:店铺；1：平台
     */
    private String isPlatform;
    /**
     * 满多少钱
     */
    private BigDecimal completely;

    private String giveMemberListId;
    /**
     * 0:购买使用；1：核销使用
     */
    private String verification;
    @Excel(name = "使用人限制；会员类型数据字典：member_type", width = 15, dicCode = "member_type")
    @ApiModelProperty(value = "使用人限制；会员类型数据字典：member_type")
    @Dict(dicCode = "member_type")
    private String userRestrict;
    /**是否唯一；0：否；1：是*/
    @Excel(name = "是否唯一；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否唯一；0：否；1：是")
    private String isUniqueness;

    /********新增代码***********/
    /**
     * 会员电话号码
     */
    private String phone;
    /**
     * 会员昵称
     */
    private String nickName;
    /**
     * 优惠内容
     */
    private String discountExplain;
    /**
     * 有效期
     */
    private String startEndTime;
    /**
     * 满减多少钱
     */
    private String subtract;
    /**
     * 商品个数
     */
    private String mdgCount;
    /**
     * 判断是否是兑换券： 0. 优惠券 1.兑换券
     */
    private String isDiscount;
    /**
     * 送出时间
     */
    @Excel(name = "送出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "送出时间")
    private Date sendTime;
    /**
     * 数量
     */
    private BigDecimal quantity;

    private List<MarketingGiftBagDiscount> marketingGiftBagDiscountList;
}
