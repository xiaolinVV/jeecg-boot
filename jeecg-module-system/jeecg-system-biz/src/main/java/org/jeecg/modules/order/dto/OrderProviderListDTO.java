package org.jeecg.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.order.entity.OrderProviderGoodRecord;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Data
public class OrderProviderListDTO {

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
     * 会员id
     */
    private String memberListId;
    /**
     * 平台订单id
     */
    private String orderListId;
    /**
     * 供应商id
     */
    private String sysUserId;
    /**
     * 订单ID
     */
    private String orderNo;
    /**
     * 快递
     */
    @Dict(dicCode = "oder_distribution")
    private String distribution;
    /**
     * 供应商运费模板id
     */

    private String providerTemplateId;
    /**
     * 运费模板名称
     */
    private String templateName;
    /**
     * 计费规则
     */
    private String accountingRules;
    /**
     * 计费方式
     */
    private String chargeMode;
    /**
     * 总数
     */
    private BigDecimal amount;
    /**
     * 配送金额
     */
    private BigDecimal shipFee;
    /**
     * 父订单id；如果没有父订单，显示：0
     */
    private String parentId;
    /**
     * 物流公司；对应数据字典
     */
    @Dict(dicCode = "logistics_company")
    private String logisticsCompany;
    /**
     * 快递单号
     */
    private String trackingNumber;
    /**
     * 供应商发货地址id
     */
    private String providerAddressIdSender;
    /**
     * 供应商退货地址id
     */
    private String providerAddressIdTui;
    /**
     * 物流跟踪信息的json保存（每次查询的时候更新）
     */
    private String logisticsTracking;
    /**
     * 商品总价
     */
    private BigDecimal goodsTotal;
    /**
     * 商品总成本价（供货价）
     */
    private BigDecimal goodsTotalCost;
    /**
     * 应付款（平台应付款项）
     */
    private BigDecimal customaryDues;
    /**
     * 平台实付款项
     */
    private BigDecimal actualPayment;
    /**
     * 订单状态；0：待付款；1：待发货（已付款、部分发货）；2：待收货（已发货）；3：交易成功；4：交易失败
     */
    private String status;
    private List<OrderProviderGoodRecord> orderProviderGoodRecords;
    /**
     * 供应商名称
     */
    private String sysUserName;
    /**
     * 供应商发货地址信息
     ***/
    private ProviderAddressDTO providerAddressDTOFa;
    /**
     * 供应商退货地址信息
     ***/
    private ProviderAddressDTO providerAddressDTOTui;

    private List<OrderProviderGoodRecordDTO> orderProviderGoodRecordDTOList;
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

    //订单是否发送到中间件；0：未发送或不发送；1：已发送；
    private String isSend;

    /**
     * 子单信息
     */
    private List<OrderProviderListDTO> orderProviderListDTOs;

    /**
     * 平台订单信息
     */
    private OrderListDTO orderList;

    /**
     * 运费模板信息
     */
    private List<Map<String, Object>> providerTemplateMaps;

    /**
     * 评价信息
     */
    private List<OrderEvaluateDTO> orderEvaluateDTOList;

    /**
     * 留言
     */
    private String message;

    /**
     * 0：未下单；1：部分下单；2：全部下单
     */
    private String orderStatus;

    /**
     * 是否部分发货；0：否；1：是
     */
    private String isSender;


}
