package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsRecordDTO;
import org.jeecg.modules.store.entity.*;
import org.jeecg.modules.store.mapper.StoreWelfarePaymentsRecordMapper;
import org.jeecg.modules.store.service.*;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 福利金收款记录
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
@Service
@Slf4j
public class StoreWelfarePaymentsRecordServiceImpl extends ServiceImpl<StoreWelfarePaymentsRecordMapper, StoreWelfarePaymentsRecord> implements IStoreWelfarePaymentsRecordService {

    @Autowired
    @Lazy
    private IMemberListService iMemberListService;

    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    @Lazy
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    @Lazy
    private IStoreWelfarePaymentsGatheringService iStoreWelfarePaymentsGatheringService;

    @Autowired
    @Lazy
    private IStoreWelfarePaymentsRecordService iStoreWelfarePaymentsRecordService;

    @Autowired
    @Lazy
    private IStoreRechargeRecordService iStoreRechargeRecordService;

    @Autowired
    @Lazy
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Override
    @Transactional
    public Boolean storeCollectWelfare(MemberList memberList, StoreManage storeManage, BigDecimal money, String gatheringExplain) {

        iMemberListService.saveOrUpdate(memberList
                .setWelfarePayments(memberList.getWelfarePayments().subtract(money)));

        MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments()
                .setDelFlag("0")
                .setMemberListId(memberList.getId())
                .setSerialNumber(OrderNoUtils.getOrderNo())
                .setBargainPayments(money)
                .setWelfarePayments(memberList.getWelfarePayments())
                .setWeType("0")
                .setBargainTime(new Date())
                .setOperator(memberList.getNickName())
                .setIsPlatform("2")
                .setIsFreeze("0")
                .setTradeType("7")
                .setTradeStatus("5")
                ;
        if (StringUtils.isNotBlank(storeManage.getSubStoreName())){
            memberWelfarePayments.setGoAndCome(storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
        }else {
            memberWelfarePayments.setGoAndCome(storeManage.getStoreName());
        }
        iMemberWelfarePaymentsService.save(memberWelfarePayments
                .setWpExplain("福利金付款["+memberWelfarePayments.getSerialNumber()+"]")
                .setTradeNo(memberWelfarePayments.getSerialNumber())
        );
        log.info(" 会员福利金付款: " + memberWelfarePayments.getSerialNumber(),"  使用福利金:  "+money);
        StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = iStoreWelfarePaymentsGatheringService.list(new LambdaUpdateWrapper<StoreWelfarePaymentsGathering>()
                .eq(StoreWelfarePaymentsGathering::getStoreManageId, storeManage.getId())
                .eq(StoreWelfarePaymentsGathering::getStatus, "1")
        ).get(0);

        BigDecimal multiply = money.multiply(storeWelfarePaymentsGathering.getSubscriptionRatio().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN);

        iStoreManageService.saveOrUpdate(storeManage
                .setBalance(storeManage.getBalance().add(multiply)));
        StoreWelfarePaymentsRecord storeWelfarePaymentsRecord = new StoreWelfarePaymentsRecord()
                .setDelFlag("0")
                .setStoreManageId(storeManage.getId())
                .setMemberListId(memberList.getId())
                .setWelfarePayments(money)
                .setGatheringMoney(multiply)
                .setStatus("1")
                .setTradeNo(memberWelfarePayments.getSerialNumber())
                .setGatheringExplain(gatheringExplain)
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setSubscriptionRatio(storeWelfarePaymentsGathering.getSubscriptionRatio())//2020年8月22日20:01:56新增福利金兑换比例
                .setRemark("福利金收款[" + memberWelfarePayments.getSerialNumber() + "]");
        iStoreWelfarePaymentsRecordService.save(storeWelfarePaymentsRecord);
        log.info(" 商家福利金收款: " + storeWelfarePaymentsRecord.getOrderNo(),"   收入福利金:  "+storeWelfarePaymentsRecord.getWelfarePayments()," 收入余额:  "+storeWelfarePaymentsRecord.getGatheringMoney());
        StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord()
                .setDelFlag("0")
                .setStoreManageId(storeManage.getId())
                .setPayType("11")
                .setGoAndCome("0")
                .setAmount(multiply)
                .setTradeStatus("5")
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setOperator(storeManage.getBossName())
                .setRemark("福利金收款[" + storeWelfarePaymentsRecord.getOrderNo() + "]")
                .setTradeType("3");
        iStoreRechargeRecordService.save(storeRechargeRecord);
        log.info(" 商家福利金收款余额明细: " + storeRechargeRecord.getOrderNo(),"   交易金额:  "+storeRechargeRecord.getAmount());
        boolean b = iStoreAccountCapitalService.save(new StoreAccountCapital()
                .setDelFlag("0")
                .setStoreManageId(storeManage.getId())
                .setPayType("11")
                .setGoAndCome("0")
                .setAmount(multiply)
                .setOrderNo(storeRechargeRecord.getOrderNo())
                .setBalance(storeManage.getBalance())
        );
        if(b){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public IPage<StoreWelfarePaymentsRecordVO> queryPageList(Page<StoreWelfarePaymentsRecord> page, StoreWelfarePaymentsRecordDTO storeWelfarePaymentsRecordDTO) {
        return baseMapper.queryPageList(page,storeWelfarePaymentsRecordDTO);
    }
}
