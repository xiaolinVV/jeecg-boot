package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.member.entity.MemberGiveWelfarePayments;
import org.jeecg.modules.member.entity.MemberGiveWelfarePaymentsRecord;
import org.jeecg.modules.member.mapper.MemberGiveWelfarePaymentsMapper;
import org.jeecg.modules.member.mapper.MemberGiveWelfarePaymentsRecordMapper;
import org.jeecg.modules.member.service.IMemberGiveWelfarePaymentsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: 福利金可获赠数量记录
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
public class MemberGiveWelfarePaymentsRecordServiceImpl extends ServiceImpl<MemberGiveWelfarePaymentsRecordMapper, MemberGiveWelfarePaymentsRecord> implements IMemberGiveWelfarePaymentsRecordService {
    @Autowired(required = false)
    private MemberGiveWelfarePaymentsMapper memberGiveWelfarePaymentsMapper;
    @Override
    @Transactional
    public boolean setMemberGiveWelfarePaymentsRecord(MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord) {
        MemberGiveWelfarePayments memberGiveWelfarePayments = memberGiveWelfarePaymentsMapper.selectById(memberGiveWelfarePaymentsRecord.getMemberGiveWelfarePaymentsId());
        memberGiveWelfarePaymentsRecord.setId(UUIDGenerator.generate());
        if (memberGiveWelfarePaymentsRecord.getGoAndCome().equals("0")){
            memberGiveWelfarePayments.setGiveWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments().add(memberGiveWelfarePaymentsRecord.getWelfarePayments()));
            int i = memberGiveWelfarePaymentsMapper.updateById(memberGiveWelfarePayments);
            if (i==1){
                return insertMemberGiveWelfarePaymentsRecord(memberGiveWelfarePaymentsRecord
                        .setTotalWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments()));

            }else {
                return false;
            }
        }else {
            memberGiveWelfarePayments.setGiveWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments().subtract(memberGiveWelfarePaymentsRecord.getWelfarePayments()));
            int i = memberGiveWelfarePaymentsMapper.updateById(memberGiveWelfarePayments);
            if (i==1){
                return insertMemberGiveWelfarePaymentsRecord(memberGiveWelfarePaymentsRecord
                        .setTotalWelfarePayments(memberGiveWelfarePayments.getGiveWelfarePayments()));
            }else {
                return false;
            }
        }
    }

    private boolean insertMemberGiveWelfarePaymentsRecord(MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord){
        return this.save(memberGiveWelfarePaymentsRecord
                .setSerialNumber(OrderNoUtils.getOrderNo())
                .setPayTime(new Date())
                .setTradeNo(OrderNoUtils.getOrderNo())
        );

    }

}
