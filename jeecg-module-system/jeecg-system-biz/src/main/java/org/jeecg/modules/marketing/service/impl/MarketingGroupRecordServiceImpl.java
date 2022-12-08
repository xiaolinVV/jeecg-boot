package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;
import org.jeecg.modules.marketing.entity.MarketingGroupRecord;
import org.jeecg.modules.marketing.mapper.MarketingGroupRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.jeecg.modules.marketing.service.IMarketingGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Description: 参团记录
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Service
@Log
public class MarketingGroupRecordServiceImpl extends ServiceImpl<MarketingGroupRecordMapper, MarketingGroupRecord> implements IMarketingGroupRecordService {


    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;

    @Autowired
    @Lazy
    private IMarketingGroupManageService iMarketingGroupManageService;

    @Autowired
    private IMarketingGroupGoodListService iMarketingGroupGoodListService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Override
    public List<Map<String, Object>> getMarketingGroupRecordByMarketingGroupManageId(String marketingGroupManageId) {
        return baseMapper.getMarketingGroupRecordByMarketingGroupManageId(marketingGroupManageId);
    }

    @Override
    @Transactional
    public void randomMarketingGroupRecord(String marketingGroupManageId) {
        //规则信息
        MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());
        List<MarketingGroupRecord> marketingGroupRecords=this.list(new LambdaQueryWrapper<MarketingGroupRecord>().eq(MarketingGroupRecord::getMarketingGroupManageId,marketingGroupManageId));
        MarketingGroupManage marketingGroupManage=iMarketingGroupManageService.getById(marketingGroupManageId);
        //获取随机中奖数
        int randomint=RandomUtils.nextInt(0,marketingGroupRecords.size()-1);
        log.info("中奖拼团随机号："+randomint);
        //获取中奖记录
        MarketingGroupRecord marketingGroupRecord=marketingGroupRecords.get(randomint);
        //设置中奖数据
        marketingGroupRecord.setRewardType("1");//中奖类型；0：积分；1：购买资格
        marketingGroupRecord.setRewardStatus("1");//中奖状态；0：未中奖；1：已中奖
        marketingGroupRecord.setRewardNumber(marketingGroupRecord.getQuantity());
        marketingGroupRecord.setDeadline(marketingGroupBaseSetting.getValidity());
        Calendar calendar=Calendar.getInstance();
        marketingGroupRecord.setBuyStartTime(calendar.getTime());
        calendar.add(Calendar.HOUR,marketingGroupRecord.getDeadline().intValue());
        marketingGroupRecord.setBuyEndTime(calendar.getTime());
        this.saveOrUpdate(marketingGroupRecord);
        //获取分配的积分
        MarketingGroupGoodList marketingGroupGoodList=iMarketingGroupGoodListService.getById(marketingGroupManage.getMarketingGroupGoodListId());
        BigDecimal tuxedoWelfarePayments =marketingGroupGoodList.getTuxedoWelfarePayments().multiply(marketingGroupManage.getReturnProportion().divide(new BigDecimal(100)));
        //分配奖励的积分
        for(int i=0;i<marketingGroupRecords.size();i++){
            MarketingGroupRecord mgr=marketingGroupRecords.get(i);
            //如果是中奖数据就跳过
            if(marketingGroupRecord.getId().equals(mgr.getId())){
                continue;
            }
            if(tuxedoWelfarePayments.doubleValue()<=0){
                continue;
            }
            //如果是最后一条就赋值全部
            if((marketingGroupRecords.size()-1)==i){
                mgr.setRewardNumber(marketingGroupGoodList.getTuxedoWelfarePayments().add(tuxedoWelfarePayments));
            }else{
                //奖励其他的数据
                BigDecimal subWelfarePayments=tuxedoWelfarePayments.multiply(new BigDecimal(RandomUtils.nextInt(0,100)).divide(new BigDecimal(100)));
                tuxedoWelfarePayments=tuxedoWelfarePayments.subtract(subWelfarePayments);
                mgr.setRewardNumber(marketingGroupGoodList.getTuxedoWelfarePayments().add(subWelfarePayments));
            }
            this.saveOrUpdate(mgr);
            if(mgr.getRewardNumber().doubleValue()>0) {
                //加入用户积分
                iMemberWelfarePaymentsService.addWelfarePayments(mgr.getMemberListId(),mgr.getRewardNumber(),"10",OrderNoUtils.getOrderNo(),"");
            }
        }

    }

    @Override
    public IPage<Map<String, Object>> getMarketingGroupRecordByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingGroupRecordByMemberId(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingGroupRecordList(Page<Map<String, Object>> page, Map<String, String> paramMap) {
        return baseMapper.getMarketingGroupRecordList(page,paramMap);
    }

    @Override
    public List<Map<String, Object>> getMarketingGroupRecordByRewardStatus() {
        return baseMapper.getMarketingGroupRecordByRewardStatus();
    }

    @Override
    public Map<String, Object> getMarketingGroupRecordById(String marketingGroupRecordId) {
        return baseMapper.getMarketingGroupRecordById(marketingGroupRecordId);
    }
}
