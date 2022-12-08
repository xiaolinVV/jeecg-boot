package org.jeecg.modules.order.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Data
@TableName("order_store_list")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_store_list对象", description="商品订单列表")
public class OrderStoreListVO {

	/**主键ID*/
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	private java.util.Date updateTime;
	/**创建年*/
	private Integer year;
	/**创建月*/
	private Integer month;
	/**创建日*/
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;
	/**会员列表id*/

	private String memberListId;
	/**订单号*/

	private String orderNo;
	/**订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；*/
	@Dict(dicCode = "order_type")
	private String orderType;
	/**收货人*/
	private String consignee;
	/**联系电话*/
	private String contactNumber;
	/**收货地址*/
	private String shippingAddress;
	/**门牌号*/
	private String houseNumber;
	/**留言*/
	private String message;
	/**商品总价*/
	private BigDecimal goodsTotal;
	/**优惠金额*/
	private BigDecimal coupon;
	/**配送金额*/
	private BigDecimal shipFee;
	/**优惠券id*/
	private String marketingDiscountCouponId;
	/**应付款（支付前标准金额）*/
	private BigDecimal customaryDues;
	/**实付款（支付后标准金额）*/
	private BigDecimal actualPayment;
	/**有无修改地址（0：无修改地址；1：有修改地址）*/
	private String isUpdateAddr;
	/**订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败*/
	private String status;
	/**付款时间*/
	private java.util.Date payTime;
	/**首次发货时间*/
	private java.util.Date shipmentsTime;
	/**确认收货时间*/
	private java.util.Date deliveryTime;
	/**子订单数量*/

	private BigDecimal childOrder;
	/**关闭类型；0：超时关闭；1：买家关闭；2：卖家关闭；数据字典：oder_close_type；*/
	@Dict(dicCode = "oder_close_type")
	private String closeType;
	/**订单关闭原因；超时关闭：原因0：超时未付款；买家关闭：原因1：订单不能按预计时间送达；原因2：操作有误（商品、地址等选错）；原因3：重复下单/误下单；原因4：其他渠道价格更低；原因5：不想买；原因6：商品无货；原因7：其他原因；卖家关闭：原因8：未及时付款；原因9：买家不想买了 ；原因10：买家信息填写错误，重新拍；原因11：恶意买家/同行捣乱；原因12：缺货；原因13：同城见面交易；原因14：其他原因；数据字典：oder_close_explain*/
	@Dict(dicCode = "oder_close_explain")
	private String closeExplain;
	/**关闭时间*/
	private java.util.Date closeTime;
	/**推广人*/

	private String promoter;
	/**订单配送方式；0：快递；1：自提；2：配送；数据字典：oder_distribution*/
	@Dict(dicCode = "store_distribution_type")
	private String distribution;
	/**订单完成时间*/
	private java.util.Date completionTime;
	/**物流星级*/
	private BigDecimal logisticsStar;
	/**发货星级*/
	private BigDecimal shippingStar;
	/**服务星级*/
	private BigDecimal serviceStar;
	/**评价时间*/
	private java.util.Date evaluateTime;
	/**是否评价；0：未评价；1：已评价*/

	private String isEvaluate;
	/**成本价（供货价）*/
	private BigDecimal costPrice;
	/**商品利润*/

	private BigDecimal profit;
	/**分销佣金*/

	private BigDecimal distributionCommission;
	/**净利润*/
	private BigDecimal retainedProfits;
	/**店铺id*/
	private String sysUserId;
	/**是否部分发货；0：否；1：是*/

	private String isSender;
	/**商品总件数*/
	private BigDecimal allNumberUnits;

	/**会员信息*/
	private MemberList memberList;
	/**优惠券信息*/
	private MarketingDiscountCoupon marketingDiscountCoupon;
	/**供应商列表*/


	private List<OrderStoreSubListVO> OrderProviderListVOs;

	/**供应商订单商品记录*/
	private List<OrderStoreGoodRecord> orderStoreGoodRecords ;//orderProviderGoodRecords
	/**查询条件关闭时间*/
	private String closeTime_begin;
	/**查询条件关闭时间*/
	private String closeTime_end;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/*商品总件数*/
	public BigDecimal  allNmberUnits;
	/*福利金*/
	public BigDecimal welfarePayments;
	/**会员名称*/
	private String nickName;

	//付款时间
	private String payTime_begin;
	//付款时间
	private String payTime_end;
	// 发货时间
	private String shipmentsTime_begin;
	//发货时间
	private String shipmentsTime_end;
	//收货时间
	private String deliveryTime_begin;
	//收货时间
	private String deliveryTime_end;
	// 交易流水号
	private String serialNumber;
	//买家账号
	private String memberPhone;
	//店铺合分店名称;
	public String storeName;
	/**店铺运费模板*/
	private List<Map<String,Object>> storeTemplateMaps;
}
