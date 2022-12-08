package org.jeecg.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderListDTO {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    /**
     * 创建人
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 会员列表id
     */
    private String memberListId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单类型；数据字段获取
     */
    @Excel(name = "订单类型；数据字段获取", width = 15, dicCode = "order_type")
    @ApiModelProperty(value = "订单类型；数据字段获取")
    @Dict(dicCode = "order_type")
    private String orderType;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 联系电话
     */
    private String contactNumber;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 门牌号
     */
    private String houseNumber;
    /**
     * 留言
     */
    private String message;
    /**
     * 商品总价
     */
    private BigDecimal goodsTotal;
    /**
     * 优惠金额
     */
    private BigDecimal coupon;
    /**
     * 配送金额
     */
    private BigDecimal shipFee;
    /**
     * 优惠券id
     */
    private String marketingDiscountCouponId;
    /**
     * 应付款（支付前标准金额）
     */
    private BigDecimal customaryDues;
    /**
     * 实付款（支付后标准金额）
     */
    private BigDecimal actualPayment;
    /**
     * 有无修改地址（0：无修改地址；1：有修改地址）
     */
    private String isUpdateAddr;
    /**
     * 订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败
     */
    private String status;
    /**
     * 付款时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    /**
     * 首次发货时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shipmentsTime;
    /**
     * 确认收货时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
    /**
     * 子订单数量
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private BigDecimal childOrder;
    /**
     * 关闭类型;查看数据字典
     */
    @Dict(dicCode = "oder_close_type")
    private String closeType;
    /**
     * 关闭原因
     */
    @Dict(dicCode = "oder_close_explain")
    private String closeExplain;
    /**
     * 关闭时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;
    /**
     * 推广人
     */
    private String promoter;
    /**
     * 归属店铺
     */
    private String affiliationStore;
    /**
     * 销售渠道
     */
    private String distributionChannel;
    /**
     * 配送方式；对应数据字典
     */
    @Dict(dicCode = "oder_distribution")
    private String distribution;
    /**
     * 是否部分发货；0：否；1：是
     */
    private String isSender;
    /**
     * 订单完成时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completionTime;
    /**
     * 物流星级
     */
    private BigDecimal logisticsStar;
    /**
     * 发货星级
     */
    private BigDecimal shippingStar;
    /**
     * 服务星级
     */
    private BigDecimal serviceStar;
    /**
     * 评价时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date evaluateTime;
    /**
     * 是否评价；0：未评价；1：已评价
     */
    private String isEvaluate;
    /**
     * 成本价（供货价）
     */
    private BigDecimal costPrice;
    /**
     * 商品利润（销售价减去成本价）
     */
    private BigDecimal profit;
    /**
     * 分销佣金
     */
    private BigDecimal distributionCommission;
    /**
     * 净利润
     */
    private BigDecimal retainedProfits;
    /**
     * 平台净利润（供货价减去平台成本价）
     */
    private BigDecimal platformRetainedProfits;
    /**
     * 平台成本价
     */
    private BigDecimal platformProfit;
    /**
     * 会员信息
     */
    private MemberList memberList;
    /**
     * 优惠券信息
     */
    private MarketingDiscountCoupon marketingDiscountCoupon;
    /**
     * 供应商列表DTO
     */
    private List<OrderProviderListDTO> OrderProviderListDTOs;
    /**
     * 供应商订单商品记录
     */
    private List<OrderProviderGoodRecord> orderProviderGoodRecords;

    /*福利金*/
    public BigDecimal welfarePayments;
    /*商品总件数*/
    public BigDecimal allNmberUnits;
    /**
     * 评论集合
     */
    private List<OrderEvaluateDTO> orderEvaluateDTOList;
    /**
     * 会员名称
     */
    private String nickName;
    /**
     * 区域Id
     */
    private String sysAreaId;
    /**
     * 订单是否发送到中间件:0:未发送;1已发送
     */
    private String isSend;

    /**
     * 供应商订单商品记录
     */
    private String orderProviderGoodRecordListJson;
    // 交易流水号
    private String serialNumber;
    /**
     * 会员直降金额
     */
    private BigDecimal vipLowerTotal;
    //赠送福利金
    private BigDecimal giveWelfarePayments;
    //推广人
    private String promoterName;
    //销售渠道
    private String distributionChannelName;
    //买家账号
    private String memberPhone;

    /**
     * 供应商运费模板
     */
    private List<Map<String, Object>> providerTemplateMaps;

    /**
     * 会员等级名称
     */

    private String memberGrade;
    /**
     * 会员等级优惠总金额
     */
    private BigDecimal memberDiscountPriceTotal;

    /**
     * 会员等级特权信息
     */
    private String memberGradeContent;

    /**
     * 应付款(供应商查看)
     */
    private BigDecimal payablesPrivoder;


    /**
     * 支付金额
     */
    private BigDecimal payPrice;

    /**
     * 支付的福利金
     */
    private BigDecimal payWelfarePayments;


    /**
     * 支付的福利金价值
     */
    private BigDecimal payWelfarePaymentsPrice;


    /**
     * 余额
     */
    private BigDecimal balance;


    private BigDecimal integral;

    private BigDecimal integralPrice;

}
