package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingFreeOrder;
import org.jeecg.modules.marketing.entity.MarketingFreeWinningAnnouncement;
import org.jeecg.modules.marketing.mapper.MarketingFreeOrderMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeOrderService;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
import org.jeecg.modules.marketing.service.IMarketingFreeWinningAnnouncementService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 免单订单
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
public class MarketingFreeOrderServiceImpl extends ServiceImpl<MarketingFreeOrderMapper, MarketingFreeOrder> implements IMarketingFreeOrderService {

    @Autowired
    protected IMarketingFreeSessionService iMarketingFreeSessionService;

    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingFreeWinningAnnouncementService iMarketingFreeWinningAnnouncementService;

    @Autowired
    private PayUtils payUtils;

    @Override
    public boolean submitOrder(String orderId) {
        //获取当前场次信息
        Map<String,Object> marketingFreeSessionMap= iMarketingFreeSessionService.selectCurrentSession();
        this.save(new MarketingFreeOrder()
                .setDelFlag("0")
                .setOrderListId(orderId)
                .setMarketingFreeSessionId(marketingFreeSessionMap.get("id").toString()));
        return true;
    }

    @Override
    public List<Map<String,Object>> selectMarketingFreeOrderGroupByPayTime(Map<String,Object> paramMap) {
        return baseMapper.selectMarketingFreeOrderGroupByPayTime(paramMap);
    }

    @Override
    public IPage<Map<String, Object>> selectMarketingFreeOrderByMarketingFreeSessionId(Page<Map<String,Object>> page,Map<String, Object> paramMap) {
        return baseMapper.selectMarketingFreeOrderByMarketingFreeSessionId(page,paramMap);
    }

    @Override
    @Transactional
    public String freeChargeOrder(String marketingFreeOrderId) {

        //获取免单订单信息
        MarketingFreeOrder marketingFreeOrder=this.getById(marketingFreeOrderId);
        //免单条件判断
        if(marketingFreeOrder.getFreeStatus().equals("1")){
            return "本免单订单已经免单！！！";
        }
        if(marketingFreeOrder.getPayType().equals("0")){
            return "本免单订单未支付金额！！！";
        }
        //免单订单修改免单状态
        marketingFreeOrder.setFreeStatus("1");
        marketingFreeOrder.setFreeTime(new Date());

        //修改订单禁止进入售后流程
        OrderList orderList=iOrderListService.getById(marketingFreeOrder.getOrderListId());
        orderList.setIsAfterSale("0");
        //订单款项退回
        Map<String, Object> response =payUtils.refund(orderList.getPayPrice(),orderList.getSerialNumber(),orderList.getHftxSerialNumber());
        marketingFreeOrder.setRefundLog(JSON.toJSONString(response));
        this.saveOrUpdate(marketingFreeOrder);
        if(response!=null&&response.get("status").equals("failed")){
            return String.valueOf(response.get("error_msg"));
        }
        //查询是否有余额支付
        if(orderList.getBalance().doubleValue()>0){
            //计算退还资金
            iMemberListService.addBlance(orderList.getMemberListId(),orderList.getBalance(),orderList.getOrderNo(),"11");
        }
        iOrderListService.saveOrUpdate(orderList);
        this.saveOrUpdate(marketingFreeOrder);
        //更新中奖公告的实际免单数
        MarketingFreeWinningAnnouncement marketingFreeWinningAnnouncement=iMarketingFreeWinningAnnouncementService.getOne(new LambdaQueryWrapper<MarketingFreeWinningAnnouncement>()
                .eq(MarketingFreeWinningAnnouncement::getMarketingFreeSessionId,marketingFreeOrder.getMarketingFreeSessionId()));
        marketingFreeWinningAnnouncement.setPracticalFreeTimes(marketingFreeWinningAnnouncement.getPracticalFreeTimes().add(new BigDecimal(1)));
        marketingFreeWinningAnnouncement.setTotalFreeTimes(marketingFreeWinningAnnouncement.getTotalFreeTimes().add(new BigDecimal(1)).add(marketingFreeWinningAnnouncement.getPracticalFreeTimes()));
        iMarketingFreeWinningAnnouncementService.saveOrUpdate(marketingFreeWinningAnnouncement);
        //删除订单的利润空间（后续来补）

        return "SUCCESS";
    }
}
