package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManage;
import org.jeecg.modules.marketing.entity.MarketingThirdIntegralRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageService;
import org.jeecg.modules.marketing.service.IMarketingThirdIntegralRecordService;
import org.jeecg.modules.member.entity.MemberThirdIntegral;
import org.jeecg.modules.member.mapper.MemberThirdIntegralMapper;
import org.jeecg.modules.member.service.IMemberThirdIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 会员第三积分
 * @Author: jeecg-boot
 * @Date:   2021-06-30
 * @Version: V1.0
 */
@Service
@Log
public class MemberThirdIntegralServiceImpl extends ServiceImpl<MemberThirdIntegralMapper, MemberThirdIntegral> implements IMemberThirdIntegralService {

    @Autowired
    private IMarketingThirdIntegralRecordService iMarketingThirdIntegralRecordService;

    @Autowired
    private IMarketingGroupIntegralManageService iMarketingGroupIntegralManageService;

    @Override
    public BigDecimal totalIntegral(String memberId) {
        BigDecimal totalIntegral=new BigDecimal(0);
        List<MemberThirdIntegral> memberThirdIntegrals= this.list(new LambdaQueryWrapper<MemberThirdIntegral>()
                .eq(MemberThirdIntegral::getMemberListId,memberId) );
        for (MemberThirdIntegral mti:memberThirdIntegrals) {
            totalIntegral=totalIntegral.add(mti.getIntegral());
        }
        return totalIntegral;
    }

    @Override
    @Transactional
    public boolean addThirdIntegral(String marketingGroupIntegralManageId, String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员第三积分增加，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //查询第三积分数据
            long count=this.count(new LambdaQueryWrapper<MemberThirdIntegral>()
                    .eq(MemberThirdIntegral::getMemberListId,memberId)
                    .eq(MemberThirdIntegral::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId));
            MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageId);
            MemberThirdIntegral memberThirdIntegral=null;
            if(count==0){
                memberThirdIntegral=new MemberThirdIntegral()
                        .setMemberListId(memberId)
                        .setIntegral(new BigDecimal(0))
                        .setIntegralGroup(marketingGroupIntegralManage.getAnotherName())
                        .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId);
                this.save(memberThirdIntegral);
            }else{
                memberThirdIntegral=this.getOne(new LambdaQueryWrapper<MemberThirdIntegral>()
                        .eq(MemberThirdIntegral::getMemberListId,memberId)
                        .eq(MemberThirdIntegral::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId));
            }
            //增加会员积分
            memberThirdIntegral.setIntegral(memberThirdIntegral.getIntegral().add(integral));
            //形成记录

            iMarketingThirdIntegralRecordService.save(new MarketingThirdIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("0")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberThirdIntegral.getIntegral())
                    .setIntegralGroup(marketingGroupIntegralManage.getAnotherName())
                    .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId)
                    .setTotalIntegral(totalIntegral(memberId)));
            return this.updateById(memberThirdIntegral);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractThirdIntegral(String marketingGroupIntegralManageId, String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员第三积分减少，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //查询第三积分数据
            long count=this.count(new LambdaQueryWrapper<MemberThirdIntegral>()
                    .eq(MemberThirdIntegral::getMemberListId,memberId)
                    .eq(MemberThirdIntegral::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId));
            MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageId);
            MemberThirdIntegral memberThirdIntegral=null;
            if(count==0){
                memberThirdIntegral=new MemberThirdIntegral()
                        .setMemberListId(memberId)
                        .setIntegral(new BigDecimal(0))
                        .setIntegralGroup(marketingGroupIntegralManage.getAnotherName())
                        .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId);
                this.save(memberThirdIntegral);
            }else{
                memberThirdIntegral=this.getOne(new LambdaQueryWrapper<MemberThirdIntegral>()
                        .eq(MemberThirdIntegral::getMemberListId,memberId)
                        .eq(MemberThirdIntegral::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId));
            }
            //减少会员积分
            memberThirdIntegral.setIntegral(memberThirdIntegral.getIntegral().subtract(integral));
            //形成记录

            iMarketingThirdIntegralRecordService.save(new MarketingThirdIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("1")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberThirdIntegral.getIntegral())
                    .setIntegralGroup(marketingGroupIntegralManage.getAnotherName())
                    .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId)
                    .setTotalIntegral(totalIntegral(memberId)));
            return this.updateById(memberThirdIntegral);
        }
        return true;
    }
}
