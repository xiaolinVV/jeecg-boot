package org.jeecg.modules.marketing.service.impl;


import lombok.extern.java.Log;
import org.jeecg.common.util.OrderNoUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.modules.marketing.entity.MarketingIntegralRecord;
import org.jeecg.modules.marketing.mapper.MarketingIntegralRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingIntegralRecordService;

import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;

import org.jeecg.modules.marketing.vo.MarketingIntegralRecordVO;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 积分记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Service
@Log
public class MarketingIntegralRecordServiceImpl extends ServiceImpl<MarketingIntegralRecordMapper, MarketingIntegralRecord> implements IMarketingIntegralRecordService {


    @Autowired
    private IMemberListService iMemberListService;

    @Override
    @Transactional
    public boolean addMarketingIntegralRecord(String tradeType, BigDecimal integral, String memberListId,String tradeNo) {
        if(integral.doubleValue()>0){

            log.info("免费积分增加：会员："+memberListId+";交易单号："+tradeNo+";交易类型："+tradeType);
            MarketingIntegralRecord marketingIntegralRecord=new MarketingIntegralRecord();
            marketingIntegralRecord.setMemberListId(memberListId);//会员列表id
            marketingIntegralRecord.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            marketingIntegralRecord.setTradeType(tradeType);//交易类型：字典类型：0：注册成功；1：交易密码；2：连续签到；3：每日浏览；4；邀请签到；5：邀请注册
            marketingIntegralRecord.setGoAndCome("0");//支付和收入；0：收入；1：支出
            marketingIntegralRecord.setIntegral(integral);//交易积分
            marketingIntegralRecord.setTradeNo(tradeNo);//交易单号
            marketingIntegralRecord.setPayTime(new Date());//交易时间
            //加入会员积分
            MemberList memberList=iMemberListService.getById(memberListId);
            memberList.setIntegral(memberList.getIntegral().add(integral));
            marketingIntegralRecord.setIntegralBalance(memberList.getIntegral());
            this.save(marketingIntegralRecord);
            return iMemberListService.updateById(memberList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractMarketingIntegralRecord(String tradeType, BigDecimal integral, String memberListId, String tradeNo) {
        if(integral.doubleValue()>0){
            MarketingIntegralRecord marketingIntegralRecord=new MarketingIntegralRecord();
            marketingIntegralRecord.setMemberListId(memberListId);//会员列表id
            marketingIntegralRecord.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            marketingIntegralRecord.setTradeType(tradeType);//交易类型：字典类型：0：注册成功；1：交易密码；2：连续签到；3：每日浏览；4；邀请签到；5：邀请注册
            marketingIntegralRecord.setGoAndCome("1");//支付和收入；0：收入；1：支出
            marketingIntegralRecord.setIntegral(integral);//交易积分
            marketingIntegralRecord.setTradeNo(tradeNo);//交易单号
            marketingIntegralRecord.setPayTime(new Date());//交易时间
            //加入会员积分
            MemberList memberList=iMemberListService.getById(memberListId);
            memberList.setIntegral(memberList.getIntegral().subtract(integral));
            marketingIntegralRecord.setIntegralBalance(memberList.getIntegral());
            this.save(marketingIntegralRecord);
            return iMemberListService.updateById(memberList);
        }
        return true;
    }

    @Override
    public IPage<MarketingIntegralRecordVO> queryPageList(Page<MarketingIntegralRecordVO> page, QueryWrapper<MarketingIntegralRecordVO> queryWrapper,Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,queryWrapper,requestMap);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingIntegralRecordPageMap(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getMarketingIntegralRecordPageMap(page,memberId);
    }

}
