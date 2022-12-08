package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.dto.MarketingFourthIntegralRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingFourthIntegralRecord;
import org.jeecg.modules.marketing.mapper.MarketingFourthIntegralRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingFourthIntegralRecordService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 第四积分记录
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Service
@Log
public class MarketingFourthIntegralRecordServiceImpl extends ServiceImpl<MarketingFourthIntegralRecordMapper, MarketingFourthIntegralRecord> implements IMarketingFourthIntegralRecordService {

    @Autowired
    private IMemberListService iMemberListService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingFourthIntegralRecordDTO marketingFourthIntegralRecordDTO) {
        return baseMapper.queryPageList(page,marketingFourthIntegralRecordDTO);
    }

    @Override
    @Transactional
    public boolean addFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员第四积分增加，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //增加会员积分
            MemberList memberList=iMemberListService.getById(memberId);
            memberList.setFourthIntegral(memberList.getFourthIntegral().add(integral));

            //形成记录
            this.save(new MarketingFourthIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("0")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberList.getFourthIntegral()));

            return iMemberListService.saveOrUpdate(memberList);

        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员第四积分减少，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //增加会员积分
            MemberList memberList=iMemberListService.getById(memberId);
            memberList.setFourthIntegral(memberList.getFourthIntegral().subtract(integral));
            //形成记录
            this.save(new MarketingFourthIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("1")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberList.getFourthIntegral()));

            return iMemberListService.saveOrUpdate(memberList);

        }
        return true;
    }


    @Override
    @Transactional
    public boolean addVirtualFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员虚拟第四积分增加，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //增加会员积分
            MemberList memberList=iMemberListService.getById(memberId);

            //形成记录
            return this.save(new MarketingFourthIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("0")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberList.getFourthIntegral()));

        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractVirtualFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType) {
        if(integral.doubleValue()>0){
            log.info("会员虚拟第四积分减少，会员id:"+memberId+"；积分数量："+integral+"；交易单号；"+tradeNo+"；交易类型："+tradeType);
            //增加会员积分
            MemberList memberList=iMemberListService.getById(memberId);
            memberList.setFourthIntegral(memberList.getFourthIntegral().subtract(integral));
            //形成记录
            return this.save(new MarketingFourthIntegralRecord()
                    .setMemberListId(memberId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setTradeType(tradeType)
                    .setGoAndCome("1")
                    .setIntegral(integral)
                    .setPayTime(new Date())
                    .setTradeNo(tradeNo)
                    .setIntegralBalance(memberList.getFourthIntegral()));
        }
        return true;
    }


}
