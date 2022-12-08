package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.member.entity.MemberGiveWelfarePayments;
import org.jeecg.modules.member.entity.MemberGiveWelfarePaymentsRecord;
import org.jeecg.modules.member.mapper.MemberGiveWelfarePaymentsMapper;
import org.jeecg.modules.member.service.IMemberGiveWelfarePaymentsRecordService;
import org.jeecg.modules.member.service.IMemberGiveWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import java.util.Map;

/**
 * @Description: 会员福利金可获赠数量
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
public class MemberGiveWelfarePaymentsServiceImpl extends ServiceImpl<MemberGiveWelfarePaymentsMapper, MemberGiveWelfarePayments> implements IMemberGiveWelfarePaymentsService {

    @Autowired
    private IMemberGiveWelfarePaymentsRecordService iMemberGiveWelfarePaymentsRecordService;


    @Override
    @Transactional
    public boolean add(String memberId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()!=0){
            long count=this.count(new LambdaQueryWrapper<MemberGiveWelfarePayments>().eq(MemberGiveWelfarePayments::getMemberListId,memberId));
            MemberGiveWelfarePayments memberGiveWelfarePayments=null;
            if(count==0){
                memberGiveWelfarePayments=new MemberGiveWelfarePayments();
                memberGiveWelfarePayments.setMemberListId(memberId);
                memberGiveWelfarePayments.setGiveWelfarePayments(new BigDecimal(0));
                this.save(memberGiveWelfarePayments);
            }else{
                memberGiveWelfarePayments=this.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                        .eq(MemberGiveWelfarePayments::getMemberListId,memberId)
                        .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                        .last("limit 1"));
            }
            memberGiveWelfarePayments.setGiveWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments().add(welfarePayments));
            MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord=new MemberGiveWelfarePaymentsRecord();
            memberGiveWelfarePaymentsRecord.setMemberGiveWelfarePaymentsId(memberGiveWelfarePayments.getId());
            memberGiveWelfarePaymentsRecord.setSerialNumber(OrderNoUtils.getOrderNo());
            memberGiveWelfarePaymentsRecord.setTradeType(tradeType);
            memberGiveWelfarePaymentsRecord.setGoAndCome("0");
            memberGiveWelfarePaymentsRecord.setWelfarePayments(welfarePayments);
            memberGiveWelfarePaymentsRecord.setTotalWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments());
            memberGiveWelfarePaymentsRecord.setPayTime(new Date());
            memberGiveWelfarePaymentsRecord.setTradeNo(tradeNo);
            iMemberGiveWelfarePaymentsRecordService.save(memberGiveWelfarePaymentsRecord);
            return this.saveOrUpdate(memberGiveWelfarePayments);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtract(String memberId, BigDecimal welfarePayments, String tradeType, String tradeNo) {
        if(welfarePayments.doubleValue()!=0){
            long count=this.count(new LambdaQueryWrapper<MemberGiveWelfarePayments>().eq(MemberGiveWelfarePayments::getMemberListId,memberId));
            MemberGiveWelfarePayments memberGiveWelfarePayments=null;
            if(count==0){
                memberGiveWelfarePayments=new MemberGiveWelfarePayments();
                memberGiveWelfarePayments.setMemberListId(memberId);
                memberGiveWelfarePayments.setGiveWelfarePayments(new BigDecimal(0));
                this.save(memberGiveWelfarePayments);
            }else{
                memberGiveWelfarePayments=this.getOne(new LambdaQueryWrapper<MemberGiveWelfarePayments>()
                        .eq(MemberGiveWelfarePayments::getMemberListId,memberId)
                        .orderByDesc(MemberGiveWelfarePayments::getCreateTime)
                        .last("limit 1"));
            }
            memberGiveWelfarePayments.setGiveWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments().subtract(welfarePayments));
            MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord=new MemberGiveWelfarePaymentsRecord();
            memberGiveWelfarePaymentsRecord.setMemberGiveWelfarePaymentsId(memberGiveWelfarePayments.getId());
            memberGiveWelfarePaymentsRecord.setSerialNumber(OrderNoUtils.getOrderNo());
            memberGiveWelfarePaymentsRecord.setTradeType(tradeType);
            memberGiveWelfarePaymentsRecord.setGoAndCome("1");
            memberGiveWelfarePaymentsRecord.setWelfarePayments(welfarePayments);
            memberGiveWelfarePaymentsRecord.setTotalWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments());
            memberGiveWelfarePaymentsRecord.setPayTime(new Date());
            memberGiveWelfarePaymentsRecord.setTradeNo(tradeNo);
            iMemberGiveWelfarePaymentsRecordService.save(memberGiveWelfarePaymentsRecord);
            return this.saveOrUpdate(memberGiveWelfarePayments);
        }
        return true;
    }
    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> requestMap) {
        return baseMapper.queryPageList(page,requestMap);
    }
}
