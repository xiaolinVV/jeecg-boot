package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.alliance.dto.AllianceSettleAccountsDTO;
import org.jeecg.modules.alliance.entity.*;
import org.jeecg.modules.alliance.mapper.AllianceSettleAccountsMapper;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.service.IAllianceRechargeRecordService;
import org.jeecg.modules.alliance.service.IAllianceSettleAccountsService;
import org.jeecg.modules.alliance.vo.AllianceSettleAccountsVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 加盟商提现
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@Service
public class AllianceSettleAccountsServiceImpl extends ServiceImpl<AllianceSettleAccountsMapper, AllianceSettleAccounts> implements IAllianceSettleAccountsService {
    @Autowired
    @Lazy
    private IAllianceRechargeRecordService iAllianceRechargeRecordService;
    @Autowired
    @Lazy
    private IAllianceManageService iAllianceManageService;
    @Autowired
    @Lazy
    private ISysUserService iSysUserService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;
    @Override
    public IPage<AllianceSettleAccountsVO> queryPageList(Page<AllianceSettleAccounts> page, AllianceSettleAccountsDTO allianceSettleAccountsDTO) {
        return baseMapper.queryPageList(page,allianceSettleAccountsDTO);
    }

    @Override
    @Transactional
    public void cashOut(AllianceBankCard allianceBankCard, AllianceManage allianceManage, BigDecimal amount, String remark) {
        AllianceManage allianceManage1 = allianceManage
                .setBalance(allianceManage.getBalance().subtract(amount))
                .setUnusableFrozen(allianceManage.getUnusableFrozen().add(amount));
        iAllianceManageService.saveOrUpdate(allianceManage1);
        SysUser user = iSysUserService.getById(allianceManage.getSysUserId());
        String s = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "withdrawal_service_charge"), "%");
        BigDecimal multiply = amount.multiply(new BigDecimal(s).divide(new BigDecimal(100)));
        AllianceSettleAccounts allianceSettleAccounts = new AllianceSettleAccounts()
                .setDelFlag("0")
                .setSysUserId(allianceManage.getSysUserId())
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setPhone(user.getPhone())
                .setMoney(amount)
                .setServiceCharge(multiply)
                .setAmount(amount.subtract(multiply))
                .setStatus("0")
                .setRemark(remark)
                .setBankCard(allianceBankCard.getBankCard())
                .setBankName(allianceBankCard.getBankName())
                .setCardholder(allianceBankCard.getCardholder())
                ;
        if (allianceBankCard.getCarType().equals("0")) {
            allianceSettleAccounts.setWithdrawalType("2");//提现类型(银行卡)
        } else if (allianceBankCard.getCarType().equals("1")) {
            allianceSettleAccounts.setWithdrawalType("1");//提现类型(支付宝)
        } else {
            allianceSettleAccounts.setWithdrawalType("0");//提现类型(微信)
        }
        this.save(allianceSettleAccounts);
        iAllianceRechargeRecordService.save(new AllianceRechargeRecord()
                .setDelFlag("0")
                .setSysUserId(allianceManage.getSysUserId())
                .setPayType("1")
                .setGoAndCome("1")
                .setAmount(allianceSettleAccounts.getAmount())
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setTradeStatus("1")
                .setTradeNo(allianceSettleAccounts.getOrderNo())
                .setBankCard(allianceSettleAccounts.getBankCard())
                .setBankName(allianceSettleAccounts.getBankName())
                .setCardholder(allianceSettleAccounts.getCardholder())
                .setRemark("余额提现["+allianceSettleAccounts.getOrderNo()+"]")
        );
    }

    @Override
    @Transactional
    public void audit(AllianceSettleAccounts allianceSettleAccounts) {
        AllianceSettleAccounts settleAccounts = this.getById(allianceSettleAccounts.getId());
        if (allianceSettleAccounts.getStatus().equals("1")){
            this.saveOrUpdate(settleAccounts
                    .setStatus("1")
                    .setTimeApplication(new Date())
            );
            iAllianceRechargeRecordService.update(new AllianceRechargeRecord()
                    .setTradeStatus("3"),new LambdaUpdateWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getSysUserId,allianceSettleAccounts.getSysUserId())
                    .eq(AllianceRechargeRecord::getTradeNo,allianceSettleAccounts.getOrderNo())
            );
        }
        if (allianceSettleAccounts.getStatus().equals("3")){

            this.saveOrUpdate(settleAccounts
                    .setStatus("3")
                    .setCloseExplain(allianceSettleAccounts.getCloseExplain()));

            AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>()
                    .eq(AllianceManage::getSysUserId, settleAccounts.getSysUserId()));

            iAllianceManageService.saveOrUpdate(allianceManage
                    .setUnusableFrozen(allianceManage.getUnusableFrozen().subtract(settleAccounts.getAmount()))
                    .setBalance(allianceManage.getBalance().add(settleAccounts.getAmount()))
            );
            iAllianceRechargeRecordService.update(new AllianceRechargeRecord()
                    .setTradeStatus("7"),new LambdaUpdateWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getSysUserId,settleAccounts.getSysUserId())
                    .eq(AllianceRechargeRecord::getTradeNo,settleAccounts.getOrderNo()));
        }

    }

    @Override
    @Transactional
    public void remit(AllianceSettleAccounts allianceSettleAccounts) {
        AllianceSettleAccounts settleAccounts = this.getById(allianceSettleAccounts.getId());
        if (allianceSettleAccounts.getStatus().equals("2")){
            this.saveOrUpdate(settleAccounts
                    .setStatus("2")
                    .setPayTime(new Date())
                    .setRemark(allianceSettleAccounts.getRemark())
            );

            AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaQueryWrapper<AllianceManage>()
                    .eq(AllianceManage::getSysUserId, settleAccounts.getSysUserId()));

            iAllianceManageService.saveOrUpdate(allianceManage
                    .setUnusableFrozen(allianceManage.getUnusableFrozen().subtract(settleAccounts.getAmount())));

            AllianceRechargeRecord allianceRechargeRecord = iAllianceRechargeRecordService.getOne(new LambdaQueryWrapper<AllianceRechargeRecord>()
                    .eq(AllianceRechargeRecord::getSysUserId, settleAccounts.getSysUserId())
                    .eq(AllianceRechargeRecord::getTradeNo, settleAccounts.getOrderNo()));

            iAllianceRechargeRecordService.saveOrUpdate(allianceRechargeRecord.setTradeStatus("5"));

            iAllianceAccountCapitalService.save(new AllianceAccountCapital()
                    .setDelFlag("0")
                    .setSysUserId(allianceManage.getSysUserId())
                    .setPayType("1")
                    .setGoAndCome("1")
                    .setAmount(settleAccounts.getAmount())
                    .setOrderNo(allianceRechargeRecord.getOrderNo())
                    .setBalance(allianceManage.getBalance())
            );
        }
    }
}
