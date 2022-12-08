package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.provider.dto.ProvideSettleAccountsDTO;
import org.jeecg.modules.provider.entity.ProvideSettleAccounts;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.mapper.ProvideSettleAccountsMapper;
import org.jeecg.modules.provider.service.IProvideSettleAccountsService;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.service.IProviderRechargeRecordService;
import org.jeecg.modules.provider.vo.ProvideSettleAccountsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: 供应商待付款
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
@Service
public class ProvideSettleAccountsServiceImpl extends ServiceImpl<ProvideSettleAccountsMapper, ProvideSettleAccounts> implements IProvideSettleAccountsService {
   @Autowired(required = false)
   private ProvideSettleAccountsMapper provideSettleAccountsMapper;
   @Autowired
   private IProviderRechargeRecordService iProviderRechargeRecordService;
   @Autowired
   private IProviderManageService iProviderManageService;
   @Autowired
   private IProviderAccountCapitalService iProviderAccountCapitalService;
    /**
     * 贷款结算列表
     * @param page
     * @param provideSettleAccountsVO
     * @return
     */
    @Override
   public IPage<ProvideSettleAccountsDTO> getProvideSettleAccountsList(Page<ProvideSettleAccounts> page, @Param("provideSettleAccountsVO") ProvideSettleAccountsVO provideSettleAccountsVO){
        return provideSettleAccountsMapper.getProvideSettleAccountsList(page,provideSettleAccountsVO);
    }

    /**
     * 审核
     * @param provideSettleAccountsVO
     * @return
     */
    @Override
    @Transactional
    public Result<ProvideSettleAccounts> audit(ProvideSettleAccountsVO provideSettleAccountsVO) {
        Result<ProvideSettleAccounts> result = new Result<>();
        ProvideSettleAccounts provideSettleAccounts = new ProvideSettleAccounts();
        BeanUtils.copyProperties(provideSettleAccountsVO,provideSettleAccounts);
        provideSettleAccounts.setAuditTime(new Date());
        //修改状态
        boolean b = this.updateById(provideSettleAccounts);
        if (provideSettleAccountsVO.getStatus().equals("1")){
            //查出店铺余额记录
            ProviderRechargeRecord rechargeRecord = iProviderRechargeRecordService.getOne(new LambdaQueryWrapper<ProviderRechargeRecord>()
                    .eq(ProviderRechargeRecord::getTradeNo, provideSettleAccountsVO.getOrderNo())
                    .eq(ProviderRechargeRecord::getSysUserId, provideSettleAccountsVO.getSysUserId()));
            if (oConvertUtils.isEmpty(rechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //修改店铺余额记录
            rechargeRecord.setTradeStatus("3");
            iProviderRechargeRecordService.updateById(rechargeRecord);
        }else if (provideSettleAccountsVO.getStatus().equals("3")){
            //查出店铺余额记录
            ProviderRechargeRecord rechargeRecord = iProviderRechargeRecordService.getOne(new LambdaQueryWrapper<ProviderRechargeRecord>()
                    .eq(ProviderRechargeRecord::getTradeNo, provideSettleAccountsVO.getOrderNo())
                    .eq(ProviderRechargeRecord::getSysUserId, provideSettleAccountsVO.getSysUserId()));
            if (oConvertUtils.isEmpty(rechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //修改店铺余额记录
            rechargeRecord.setTradeStatus("6");
            iProviderRechargeRecordService.updateById(rechargeRecord);
            //查出提现店铺
            ProviderManage providerManage = iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>()
                    .eq(ProviderManage::getSysUserId,provideSettleAccountsVO.getSysUserId()));
            //退还店铺提现金额
            providerManage.setBalance(providerManage.getBalance().add(rechargeRecord.getAmount()));
            //交易失败从不可用金额退还
            providerManage.setUnusableFrozen(providerManage.getUnusableFrozen().subtract(rechargeRecord.getAmount()));
            iProviderManageService.updateById(providerManage);
        }else {
            return result.error500("操作异常,请重试!");
        }
        if (b){
            result.setCode(200);
            result.setMessage("操作成功!");
        }else {
            result.error500("操作失败!");
        }
        return result;
    }

    /**
     * 打款
     * @param provideSettleAccountsVO
     * @return
     */
    @Override
    @Transactional
    public Result<ProvideSettleAccounts> remit(ProvideSettleAccountsVO provideSettleAccountsVO) {
        Result<ProvideSettleAccounts> result = new Result<>();
        ProvideSettleAccounts provideSettleAccounts = new ProvideSettleAccounts();
        BeanUtils.copyProperties(provideSettleAccountsVO,provideSettleAccounts);
        if (provideSettleAccountsVO.getStatus().equals("2")){
            //查出店铺余额记录
            ProviderRechargeRecord rechargeRecord = iProviderRechargeRecordService.getOne(new LambdaQueryWrapper<ProviderRechargeRecord>()
                    .eq(ProviderRechargeRecord::getTradeNo, provideSettleAccountsVO.getOrderNo())
                    .eq(ProviderRechargeRecord::getSysUserId, provideSettleAccountsVO.getSysUserId()));
            if (oConvertUtils.isEmpty(rechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //交易成功写入余额记录
            rechargeRecord.setTradeStatus("5");
            iProviderRechargeRecordService.updateById(rechargeRecord);

            ProviderManage providerManage = iProviderManageService.getOne(new LambdaQueryWrapper<ProviderManage>()
                    .eq(ProviderManage::getSysUserId, provideSettleAccounts.getSysUserId()));
            //交易成功扣除不可用金额
            providerManage.setUnusableFrozen(providerManage.getUnusableFrozen().subtract(rechargeRecord.getAmount()));
            iProviderManageService.updateById(providerManage);
            //交易成功形成店铺资金流水记录
            ProviderAccountCapital providerAccountCapital = new ProviderAccountCapital();
            providerAccountCapital.setDelFlag("0");
            providerAccountCapital.setSysUserId(rechargeRecord.getSysUserId());
            providerAccountCapital.setPayType("1");
            providerAccountCapital.setGoAndCome("1");
            providerAccountCapital.setAmount(rechargeRecord.getAmount());
            providerAccountCapital.setOrderNo(rechargeRecord.getOrderNo());
            providerAccountCapital.setBalance(providerManage.getBalance());
            iProviderAccountCapitalService.save(providerAccountCapital);
            provideSettleAccounts.setPayTime(new Date());

        }else if (provideSettleAccountsVO.getStatus().equals("1")){
            result.setMessage("操作成功!");
        }else {
            return result.error500("操作失败,请重试!");
        }
        boolean b = this.updateById(provideSettleAccounts);
        if (b){
            result.setCode(200);
            result.setMessage("操作成功!");
        }else {
            result.error500("操作失败!");
        }
        return result;
    }

    ;


}
