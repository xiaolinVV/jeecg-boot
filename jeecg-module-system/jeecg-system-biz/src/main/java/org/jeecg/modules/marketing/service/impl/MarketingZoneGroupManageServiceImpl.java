package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupManageMapper;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.pay.entity.PayZoneGroupLog;
import org.jeecg.modules.pay.service.IPayZoneGroupLogService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
@Service
@Log
public class MarketingZoneGroupManageServiceImpl extends ServiceImpl<MarketingZoneGroupManageMapper, MarketingZoneGroupManage> implements IMarketingZoneGroupManageService {

    @Autowired
    private IPayZoneGroupLogService iPayZoneGroupLogService;

    @Autowired
    private IMarketingZoneGroupTimeService iMarketingZoneGroupTimeService;

    @Autowired
    private IMarketingZoneSpellGroupRecordService iMarketingZoneSpellGroupRecordService;

    @Autowired
    @Lazy
    private IMarketingZoneGroupService iMarketingZoneGroupService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMarketingZoneGroupGoodService iMarketingZoneGroupGoodService;

    @Autowired
    private IMarketingZoneGroupRecordService iMarketingZoneGroupRecordService;

    @Autowired
    private IMarketingZoneGroupGroupingService iMarketingZoneGroupGroupingService;

    @Autowired
    private IMarketingZoneGroupOrderService iMarketingZoneGroupOrderService;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;

    @Autowired
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private IMarketingIntegralTaskService iMarketingIntegralTaskService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingZoneGroupManage> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    @Transactional
    public void success(String payZoneGroupLogId) {
        //记录值修改
        PayZoneGroupLog payZoneGroupLog=iPayZoneGroupLogService.getById(payZoneGroupLogId);
        payZoneGroupLog.setPayStatus("1");
        iPayZoneGroupLogService.saveOrUpdate(payZoneGroupLog);
        //余额扣除
        if(!iMemberListService.subtractBlance(payZoneGroupLog.getMemberListId(),payZoneGroupLog.getBalance(),payZoneGroupLog.getId(),"26")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if(!iMemberWelfarePaymentsService.subtractWelfarePayments(payZoneGroupLog.getMemberListId(),payZoneGroupLog.getWelfarePayments(),"26",payZoneGroupLog.getId(),"")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        //扣除专区团次数
        MarketingZoneGroupTime marketingZoneGroupTime=iMarketingZoneGroupTimeService.getOne(new LambdaQueryWrapper<MarketingZoneGroupTime>()
                .eq(MarketingZoneGroupTime::getMemberListId,payZoneGroupLog.getMemberListId())
                .orderByDesc(MarketingZoneGroupTime::getCreateTime)
                .last("limit 1"));
        if(iMarketingZoneSpellGroupRecordService.subtractMarketingZoneSpellGroupRecord(marketingZoneGroupTime.getId(),"2",new BigDecimal(1),payZoneGroupLog.getId())){
            //判断有没有可以参加的团
            long marketingZoneGroupManageCount=this.count(new LambdaQueryWrapper<MarketingZoneGroupManage>().eq(MarketingZoneGroupManage::getStatus,"0").eq(MarketingZoneGroupManage::getMarketingZoneGroupId,payZoneGroupLog.getMarketingZoneGroupId()));
            MarketingZoneGroup marketingZoneGroup=iMarketingZoneGroupService.getById(payZoneGroupLog.getMarketingZoneGroupId());
            if(marketingZoneGroupManageCount==0){
                //创建团
                int i=5;
                while (i>0){
                    MarketingZoneGroupManage marketingZoneGroupManage=new MarketingZoneGroupManage();
                    marketingZoneGroupManage.setSerialNumber(OrderNoUtils.getOrderNo());
                    marketingZoneGroupManage.setZoneName(marketingZoneGroup.getZoneName());
                    marketingZoneGroupManage.setPrice(marketingZoneGroup.getPrice());
                    marketingZoneGroupManage.setVirtualGroupMembers(marketingZoneGroup.getVirtualGroupMembers());
                    marketingZoneGroupManage.setActualGroupSize(marketingZoneGroup.getActualGroupSize());
                    marketingZoneGroupManage.setStartTime(new Date());
                    marketingZoneGroupManage.setMarketingZoneGroupId(marketingZoneGroup.getId());
                    this.save(marketingZoneGroupManage);
                    i--;
                }
            }

            //用户首次拼团次数
            long firstCount=iMarketingZoneGroupRecordService.count(new LambdaQueryWrapper<MarketingZoneGroupRecord>().eq(MarketingZoneGroupRecord::getMemberListId,payZoneGroupLog.getMemberListId()));
            if(firstCount==0){
                //会员升级，和首次拼团次数
                log.info("新用户触发团队升级");
                iMemberDistributionLevelService.teamUpgrade(payZoneGroupLog.getMemberListId());
                //首单任务
                MemberList memberList=iMemberListService.getById(payZoneGroupLog.getMemberListId());
                if(memberList.getPromoterType().equals("1")){
                    iMarketingIntegralTaskService.manyTimesDay(memberList.getPromoter(),"8");
                }
            }


            //获取当前的团信息
            MarketingZoneGroupManage marketingZoneGroupManage=this.getOne(new LambdaQueryWrapper<MarketingZoneGroupManage>()
                    .eq(MarketingZoneGroupManage::getStatus,"0")
                    .eq(MarketingZoneGroupManage::getMarketingZoneGroupId,payZoneGroupLog.getMarketingZoneGroupId())
                    .orderByAsc(MarketingZoneGroupManage::getCreateTime)
                    .last("limit 1"));
            //形成成团记录
            MarketingZoneGroupRecord marketingZoneGroupRecord=new MarketingZoneGroupRecord()
                    .setMarketingZoneGroupManageId(marketingZoneGroupManage.getId())
                    .setMemberListId(payZoneGroupLog.getMemberListId())
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTuxedoTime(new Date())
                    .setMarketingZoneGroupGoodId(payZoneGroupLog.getMarketingZoneGroupGoodId());

            MarketingZoneGroupGood marketingZoneGroupGood=iMarketingZoneGroupGoodService.getById(payZoneGroupLog.getMarketingZoneGroupGoodId());
            GoodList goodList=iGoodListService.getById(marketingZoneGroupGood.getGoodListId());
            //获取商品
            GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                    .eq(GoodSpecification::getGoodListId,marketingZoneGroupGood.getGoodListId())
                    .eq(GoodSpecification::getSpecification,payZoneGroupLog.getSpecification())
                    .last("limit 1"));
            marketingZoneGroupRecord.setGoodSpecificationId(goodSpecification.getId());
            marketingZoneGroupRecord.setSpecification(goodSpecification.getSpecification());
            marketingZoneGroupRecord.setGoodNo(goodList.getGoodNo());
            marketingZoneGroupRecord.setGoodName(goodList.getGoodName());
            marketingZoneGroupRecord.setMainPicture(goodList.getMainPicture());
            marketingZoneGroupRecord.setQuantity(payZoneGroupLog.getQuantity());
            marketingZoneGroupRecord.setPayPrice(payZoneGroupLog.getTotalPrice());
            marketingZoneGroupRecord.setPayZoneGroupLogId(payZoneGroupLogId);
            long marketingZoneGroupRecordCount=iMarketingZoneGroupRecordService.count(new LambdaQueryWrapper<MarketingZoneGroupRecord>().eq(MarketingZoneGroupRecord::getMarketingZoneGroupManageId,marketingZoneGroupManage.getId()));
            if(marketingZoneGroupRecordCount==0){
                marketingZoneGroupRecord.setIdentity("1");
            }
            if(!iMarketingZoneGroupRecordService.save(marketingZoneGroupRecord)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            long marketingZoneGroupRecordTotalCount=iMarketingZoneGroupRecordService.count(new LambdaQueryWrapper<MarketingZoneGroupRecord>().eq(MarketingZoneGroupRecord::getMarketingZoneGroupManageId,marketingZoneGroupManage.getId()));

            //判断是否中奖
            if(marketingZoneGroupManage.getStatus().equals("0")&&marketingZoneGroupManage.getActualGroupSize().intValue()<=marketingZoneGroupRecordTotalCount) {
                marketingZoneGroupManage.setEndTime(new Date());
                marketingZoneGroupManage.setStatus("1");
                marketingZoneGroupManage.setCloudsTime(new Date());
                if(this.saveOrUpdate(marketingZoneGroupManage)) {
                    //选择中奖对象
                    Map<String, Object> resultMap = iMarketingZoneGroupRecordService.getWinning(marketingZoneGroupManage.getId());
                    if (resultMap != null) {
                        MarketingZoneGroupRecord successZoneGroupRecord = iMarketingZoneGroupRecordService.getById(resultMap.get("id").toString());

                        if (successZoneGroupRecord.getStatus().equals("0")) {
                            successZoneGroupRecord.setStatus("1");
                            if (iMarketingZoneGroupRecordService.saveOrUpdate(successZoneGroupRecord)) {
                                PayZoneGroupLog suZoneGroupLog = iPayZoneGroupLogService.getById(successZoneGroupRecord.getPayZoneGroupLogId());
                                MarketingZoneGroup remarketingZoneGroup = iMarketingZoneGroupService.getById(suZoneGroupLog.getMarketingZoneGroupId());
                                //查询用户分组情况
                                MarketingZoneGroupGrouping marketingZoneGroupGrouping = iMarketingZoneGroupGroupingService.getOne(new LambdaQueryWrapper<MarketingZoneGroupGrouping>()
                                        .eq(MarketingZoneGroupGrouping::getMemberListId, successZoneGroupRecord.getMemberListId())
                                        .eq(MarketingZoneGroupGrouping::getMarketingZoneGroupId, suZoneGroupLog.getMarketingZoneGroupId())
                                        .orderByDesc(MarketingZoneGroupGrouping::getCreateTime)
                                        .last("limit 1"));
                                boolean istransformation = false;
                                if (marketingZoneGroupGrouping == null) {
                                    marketingZoneGroupGrouping = new MarketingZoneGroupGrouping();
                                    marketingZoneGroupGrouping.setMemberListId(successZoneGroupRecord.getMemberListId());
                                    marketingZoneGroupGrouping.setSerialNumber(OrderNoUtils.getOrderNo());
                                    marketingZoneGroupGrouping.setTransformationThreshold(remarketingZoneGroup.getTransformationThreshold());
                                    marketingZoneGroupGrouping.setCanConsignment(remarketingZoneGroup.getCanConsignment());
                                    marketingZoneGroupGrouping.setMarketingZoneGroupId(remarketingZoneGroup.getId());
                                    iMarketingZoneGroupGroupingService.save(marketingZoneGroupGrouping);
                                } else {
                                    long marketingZoneGroupGroupingCount = iMarketingZoneGroupOrderService.count(new LambdaQueryWrapper<MarketingZoneGroupOrder>().eq(MarketingZoneGroupOrder::getMarketingZoneGroupGroupingId, marketingZoneGroupGrouping.getId()));
                                    if (marketingZoneGroupGroupingCount >= remarketingZoneGroup.getTransformationThreshold().intValue()) {
                                        marketingZoneGroupGrouping = new MarketingZoneGroupGrouping();
                                        marketingZoneGroupGrouping.setMemberListId(successZoneGroupRecord.getMemberListId());
                                        marketingZoneGroupGrouping.setSerialNumber(OrderNoUtils.getOrderNo());
                                        marketingZoneGroupGrouping.setTransformationThreshold(remarketingZoneGroup.getTransformationThreshold());
                                        marketingZoneGroupGrouping.setCanConsignment(remarketingZoneGroup.getCanConsignment());
                                        marketingZoneGroupGrouping.setMarketingZoneGroupId(remarketingZoneGroup.getId());
                                        iMarketingZoneGroupGroupingService.save(marketingZoneGroupGrouping);
                                    }
                                    if (marketingZoneGroupGroupingCount == (remarketingZoneGroup.getTransformationThreshold().intValue() - 1)) {
                                        istransformation = true;
                                    }
                                }
                                //建立订单数据
                                MarketingZoneGroupOrder marketingZoneGroupOrder = new MarketingZoneGroupOrder();
                                marketingZoneGroupOrder.setMarketingZoneGroupManageId(successZoneGroupRecord.getMarketingZoneGroupManageId());
                                marketingZoneGroupOrder.setMarketingZoneGroupGroupingId(marketingZoneGroupGrouping.getId());
                                marketingZoneGroupOrder.setMarketingZoneGroupRecordId(successZoneGroupRecord.getId());
                                marketingZoneGroupOrder.setSerialNumber(OrderNoUtils.getOrderNo());

                                marketingZoneGroupOrder.setMessage(suZoneGroupLog.getMessage());
                                marketingZoneGroupOrder.setMarketingZoneGroupGoodId(successZoneGroupRecord.getMarketingZoneGroupGoodId());
                                MemberShippingAddress memberShippingAddress = iMemberShippingAddressService.getById(suZoneGroupLog.getMemberShippingAddressId());
                                marketingZoneGroupOrder.setConsignee(memberShippingAddress.getLinkman());
                                marketingZoneGroupOrder.setContactNumber(memberShippingAddress.getPhone());
                                marketingZoneGroupOrder.setShippingAddress(memberShippingAddress.getAreaExplan() + memberShippingAddress.getAreaAddress());
                                marketingZoneGroupOrder.setSysAreaId(memberShippingAddress.getSysAreaId());
                                marketingZoneGroupOrder.setGoodsTotal(successZoneGroupRecord.getPayPrice());
                                MarketingZoneGroupGood remarketingZoneGroupGood = iMarketingZoneGroupGoodService.getById(successZoneGroupRecord.getMarketingZoneGroupGoodId());
                                GoodList regoodList = iGoodListService.getById(remarketingZoneGroupGood.getGoodListId());
                                //获取商品
                                GoodSpecification regoodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                                        .eq(GoodSpecification::getGoodListId, remarketingZoneGroupGood.getGoodListId())
                                        .eq(GoodSpecification::getSpecification, suZoneGroupLog.getSpecification())
                                        .last("limit 1"));
                                List<Map<String, Object>> goods = Lists.newArrayList();
                                Map<String, Object> g = Maps.newHashMap();
                                g.put("goodId", regoodList.getId());
                                g.put("quantity", successZoneGroupRecord.getQuantity());
                                g.put("price", successZoneGroupRecord.getPayPrice());
                                g.put("goodSpecificationId", regoodSpecification.getId());
                                goods.add(g);
                                marketingZoneGroupOrder.setShipFee(iProviderTemplateService.calculateFreight(goods, memberShippingAddress.getSysAreaId()));
                                marketingZoneGroupOrder.setCustomaryDues(suZoneGroupLog.getPayPrice());
                                marketingZoneGroupOrder.setActualPayment(suZoneGroupLog.getPayPrice());
                                if (istransformation) {
                                    marketingZoneGroupOrder.setStatus("2");
                                } else {
                                    marketingZoneGroupOrder.setStatus("1");
                                }
                                marketingZoneGroupOrder.setPayTime(new Date());
                                marketingZoneGroupOrder.setGoodSpecificationId(regoodSpecification.getId());
                                marketingZoneGroupOrder.setQuantity(suZoneGroupLog.getQuantity());
                                marketingZoneGroupOrder.setPayZoneGroupLogId(suZoneGroupLog.getId());
                                marketingZoneGroupOrder.setMemberListId(suZoneGroupLog.getMemberListId());
                                iMarketingZoneGroupOrderService.save(marketingZoneGroupOrder);
                            }else{
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }
                    }
                }else{
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }else{
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public List<Map<String,Object>> numberTuxedo(String marketingZoneGroupId) {
        return baseMapper.numberTuxedo(marketingZoneGroupId);
    }
}
