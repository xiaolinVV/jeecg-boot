package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.mapper.GoodListMapper;
import org.jeecg.modules.good.mapper.GoodSpecificationMapper;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGrouping;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupManage;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupOrder;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupRecord;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupGroupingMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupManageMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupOrderMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupOrderService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.mapper.OrderListMapper;
import org.jeecg.modules.pay.entity.PayZoneGroupLog;
import org.jeecg.modules.pay.mapper.PayZoneGroupLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 拼中商品
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Service
public class MarketingZoneGroupOrderServiceImpl extends ServiceImpl<MarketingZoneGroupOrderMapper, MarketingZoneGroupOrder> implements IMarketingZoneGroupOrderService {
    @Autowired(required = false)
    private MarketingZoneGroupGroupingMapper marketingZoneGroupGroupingMapper;
    @Autowired(required = false)
    private MarketingZoneGroupRecordMapper marketingZoneGroupRecordMapper;
    @Autowired(required = false)
    private MarketingZoneGroupManageMapper marketingZoneGroupManageMapper;
    @Autowired(required = false)
    private PayZoneGroupLogMapper payZoneGroupLogMapper;
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;
    @Autowired(required = false)
    private OrderListMapper orderListMapper;
    @Autowired(required = false)
    private GoodSpecificationMapper goodSpecificationMapper;
    @Autowired(required = false)
    private GoodListMapper goodListMapper;

    @Autowired
    @Lazy
    private IMarketingZoneGroupManageService iMarketingZoneGroupManageService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,requestMap);
    }

    @Override
    public IPage<Map<String, Object>> marketingZoneGroupOrderRecord(Page<Map<String, Object>> page, QueryWrapper<MarketingZoneGroupOrder> queryWrapper, Map<String, Object> requestMap) {
        return baseMapper.marketingZoneGroupOrderRecord(page,queryWrapper,requestMap);
    }

    @Override
    public Map<String, Object> memberRecord(String id) {
        return baseMapper.memberRecord(id);
    }

    @Override
    public Map<String, Object> goodInfo(String id) {
        return baseMapper.goodInfo(id);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingZoneGroupOrderPageByGroupingId(Page<Map<String, Object>> page, String id) {

        IPage<Map<String, Object>> marketingZoneGroupOrderPageByGroupingId = baseMapper.getMarketingZoneGroupOrderPageByGroupingId(page, id);
        MarketingZoneGroupGrouping marketingZoneGroupGrouping = marketingZoneGroupGroupingMapper.selectById(id);
        int i = 0;
        if (baseMapper.selectCount(new LambdaQueryWrapper<MarketingZoneGroupOrder>()
                .eq(MarketingZoneGroupOrder::getMarketingZoneGroupGroupingId,id)
                .eq(MarketingZoneGroupOrder::getDelFlag,"0")
        )>=marketingZoneGroupGrouping.getTransformationThreshold().intValue()){
            i = 1;
        }
        for (Map<String, Object> map : marketingZoneGroupOrderPageByGroupingId.getRecords()) {
            map.put("isViewConsignment",i);
        }
        return marketingZoneGroupOrderPageByGroupingId;
    }

    @Override
    public Map<String, Object> getMarketingZoneGroupOrderDetails(MarketingZoneGroupOrder marketingZoneGroupOrder) {
        HashMap<String, Object> map = new HashMap<>();
        MarketingZoneGroupRecord marketingZoneGroupRecord = marketingZoneGroupRecordMapper.selectById(marketingZoneGroupOrder.getMarketingZoneGroupRecordId());
        PayZoneGroupLog payZoneGroupLog = payZoneGroupLogMapper.selectById(marketingZoneGroupRecord.getPayZoneGroupLogId());
        map.put("id",marketingZoneGroupOrder.getId());
        if (marketingZoneGroupOrder.getStatus().equals("1")){
            map.put("status","已付款");
            map.put("statusDeclare","付款成功，请耐心等待发货...");
        }else if (marketingZoneGroupOrder.getStatus().equals("2")){
            map.put("status","待发货");
            map.put("statusDeclare","待发货,马上就要发货了，请耐心等待...");
        }else if (marketingZoneGroupOrder.getStatus().equals("3")){
            map.put("status","已寄售");
            map.put("statusDeclare","已寄售,已寄售，次日凌晨系统会将寄售金额转入到您的福利金账户中。");
        }else {
            map.put("status","已完成");
            map.put("statusDeclare","交易成功,欢迎再次光临！~");
        }
        map.put("consignee",marketingZoneGroupOrder.getConsignee());
        map.put("contactNumber",marketingZoneGroupOrder.getContactNumber());
        map.put("shippingAddress",marketingZoneGroupOrder.getShippingAddress());
        map.put("message",marketingZoneGroupOrder.getMessage());
        map.put("mainPicture",marketingZoneGroupRecord.getMainPicture());
        map.put("goodName",marketingZoneGroupRecord.getGoodName());
        map.put("specification",marketingZoneGroupRecord.getSpecification());
        MarketingZoneGroupManage marketingZoneGroupManage = marketingZoneGroupManageMapper.selectById(marketingZoneGroupOrder.getMarketingZoneGroupManageId());
        GoodSpecification goodSpecification = goodSpecificationMapper.selectById(marketingZoneGroupRecord.getGoodSpecificationId());
        GoodList goodList = goodListMapper.selectById(goodSpecification.getGoodListId());
        map.put("marketPrice",goodList.getMarketPrice());
        map.put("price",marketingZoneGroupManage.getPrice());
        map.put("quantity",marketingZoneGroupRecord.getQuantity());

        map.put("serialNumber",marketingZoneGroupRecord.getSerialNumber());
        map.put("tuxedoTime", DateUtils.formatTime(marketingZoneGroupRecord.getTuxedoTime().getTime()));
        map.put("createTime",DateUtils.formatTime(marketingZoneGroupOrder.getCreateTime()));

        map.put("goodsTotal",marketingZoneGroupOrder.getGoodsTotal());
        map.put("shipFee",marketingZoneGroupOrder.getShipFee());
        map.put("customaryDues",marketingZoneGroupOrder.getCustomaryDues());
        map.put("actualPayment",marketingZoneGroupOrder.getActualPayment());
        if (payZoneGroupLog.getBalance().doubleValue()>0){
            map.put("balance","￥"+payZoneGroupLog.getBalance());
        }else {
            map.put("balance","");
        }
        if (payZoneGroupLog.getWelfarePayments().doubleValue()>0){
            BigDecimal integralValue = iMarketingWelfarePaymentsSettingService.getIntegralValue();
            map.put("welfarePayments",payZoneGroupLog.getWelfarePayments().doubleValue()+"福利金≈￥"+integralValue.doubleValue()*payZoneGroupLog.getWelfarePayments().doubleValue());
        }else {
            map.put("welfarePayments","");
        }
        map.put("payModel",payZoneGroupLog.getPayModel());
        if (payZoneGroupLog.getPayPrice().doubleValue()>0){
            map.put("payPrice","￥"+payZoneGroupLog.getPayPrice());
        }else {
            map.put("payPrice","");
        }
        map.put("orderSerialNumber",marketingZoneGroupOrder.getSerialNumber());
        if (marketingZoneGroupOrder.getPayTime()!=null){
            map.put("payTime",DateUtils.formatTime(marketingZoneGroupOrder.getPayTime()));
        }else {
            map.put("payTime","");
        }

        if (StringUtils.isNotBlank(marketingZoneGroupOrder.getOrderListId())){
            OrderList orderList = orderListMapper.selectById(marketingZoneGroupOrder.getOrderListId());
            if (orderList.getShipmentsTime()!=null){
                map.put("shipmentsTime",DateUtils.formatTime(orderList.getShipmentsTime()));
                if (orderList.getCompletionTime()!=null){
                    map.put("completionTime",DateUtils.formatTime(orderList.getCompletionTime()));
                    if (orderList.getDeliveryTime()!=null){
                        map.put("deliveryTime",DateUtils.formatTime(orderList.getDeliveryTime()));
                    }else {
                        map.put("deliveryTime","");
                    }
                }else {
                    map.put("completionTime","");
                    map.put("deliveryTime","");
                }
            }else {
                map.put("shipmentsTime","");
                map.put("completionTime","");
                map.put("deliveryTime","");
            }

        }else {
            map.put("shipmentsTime","");
            map.put("completionTime","");
        }
        if (marketingZoneGroupOrder.getConsignmentTime()!=null){
            map.put("consignmentTime",DateUtils.formatTime(marketingZoneGroupOrder.getConsignmentTime()));
            if (marketingZoneGroupOrder.getConsignmentEndTime()!=null){
                map.put("consignmentEndTime",DateUtils.formatTime(marketingZoneGroupOrder.getConsignmentEndTime()));
            }else {
                map.put("consignmentEndTime","");
            }
        }else {
            map.put("consignmentTime","");
            map.put("consignmentEndTime","");
        }
        return map;
    }

    @Override
    @Transactional
    public void consignment(String marketingZoneGroupOrderId) {
        MarketingZoneGroupOrder marketingZoneGroupOrder=this.getById(marketingZoneGroupOrderId);
        MarketingZoneGroupManage marketingZoneGroupManage=iMarketingZoneGroupManageService.getById(marketingZoneGroupOrder.getMarketingZoneGroupManageId());
        //退回到福利金
        if(!iMemberWelfarePaymentsService.addWelfarePayments(marketingZoneGroupOrder.getMemberListId(),marketingZoneGroupManage.getPrice(),"33",marketingZoneGroupOrder.getSerialNumber(),"")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }else {
            marketingZoneGroupOrder.setDistributionRewards("1");
            marketingZoneGroupOrder.setConsignmentEndTime(new Date());
            if (!this.saveOrUpdate(marketingZoneGroupOrder)) {
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

}
