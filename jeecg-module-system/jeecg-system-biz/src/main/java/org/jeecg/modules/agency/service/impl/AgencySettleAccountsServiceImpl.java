package org.jeecg.modules.agency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.dto.AgencySettleAccountsDTO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.entity.AgencySettleAccounts;
import org.jeecg.modules.agency.mapper.AgencySettleAccountsMapper;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencyRechargeRecordService;
import org.jeecg.modules.agency.service.IAgencySettleAccountsService;
import org.jeecg.modules.agency.vo.AgencySettleAccountsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: 代理提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-30
 * @Version: V1.0
 */
@Service
public class AgencySettleAccountsServiceImpl extends ServiceImpl<AgencySettleAccountsMapper, AgencySettleAccounts> implements IAgencySettleAccountsService {

 @Autowired
 @Lazy
 private IAgencyManageService iAgencyManageService;
 @Autowired
 @Lazy
 private IAgencyRechargeRecordService iAgencyRechargeRecordService;
 @Autowired
 @Lazy
 private IAgencyAccountCapitalService iAgencyAccountCapitalService;
    /**
     * 提现审核结算列表
     * @param page
     * @param agencySettleAccountsVO
     * @return
     */
    @Override
   public IPage<AgencySettleAccountsDTO> getAgencySettleAccountsList(Page<AgencySettleAccounts> page, AgencySettleAccountsVO agencySettleAccountsVO){
       return baseMapper.getAgencySettleAccountsList(page,agencySettleAccountsVO);
   }

    /**
     * 审核
     * @param agencySettleAccountsVO
     * @return
     */
    @Override
    @Transactional
    public Result<AgencySettleAccounts> audit(AgencySettleAccountsVO agencySettleAccountsVO) {
        Result<AgencySettleAccounts> result = new Result<>();
        AgencySettleAccounts agencySettleAccounts = this.getById(agencySettleAccountsVO.getId());

        if (agencySettleAccountsVO.getStatus().equals("1")){
            //查出代理余额记录
            AgencyRechargeRecord agencyRechargeRecord = iAgencyRechargeRecordService.getOne(new LambdaQueryWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo,agencySettleAccounts.getOrderNo())
                    .eq(AgencyRechargeRecord::getSysUserId,agencySettleAccounts.getSysUserId())
                    .eq(AgencyRechargeRecord::getGoAndCome,"1")
                    .eq(AgencyRechargeRecord::getTradeStatus,"1")
                    .eq(AgencyRechargeRecord::getPayType,"1"));
            if (oConvertUtils.isEmpty(agencyRechargeRecord)){
                return result.error500("信息异常");
            }
            //修改代理余额记录
            iAgencyRechargeRecordService.updateById(agencyRechargeRecord
                    .setTradeStatus("3"));
        }else if (agencySettleAccountsVO.getStatus().equals("3")){
            //查出代理余额记录
            AgencyRechargeRecord rechargeRecord = iAgencyRechargeRecordService.getOne(new LambdaQueryWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo,agencySettleAccounts.getOrderNo())
                    .eq(AgencyRechargeRecord::getSysUserId,agencySettleAccounts.getSysUserId())
                    .eq(AgencyRechargeRecord::getGoAndCome,"1")
                    .eq(AgencyRechargeRecord::getTradeStatus,"1")
                    .eq(AgencyRechargeRecord::getPayType,"1"));
            if (oConvertUtils.isEmpty(rechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }

            //修改代理余额记录
            iAgencyRechargeRecordService.updateById(rechargeRecord
                    .setTradeStatus("6"));

            //查出提现代理
            AgencyManage agencyManage = iAgencyManageService.getOne(new LambdaQueryWrapper<AgencyManage>()
                    .eq(AgencyManage::getSysUserId, agencySettleAccounts.getSysUserId()));


            iAgencyManageService.updateById(agencyManage
                    .setBalance(agencyManage.getBalance().add(rechargeRecord.getAmount()))//退还代理提现金额
                    .setUnusableFrozen(agencyManage.getUnusableFrozen().subtract(rechargeRecord.getAmount())));//提现金额移出
        }else {
            result.error500("操作失败!请重试.");
        }
        //修改状态
        BeanUtils.copyProperties(agencySettleAccountsVO,agencySettleAccounts);
        agencySettleAccounts.setAuditTime(new Date());
        boolean b = this.updateById(agencySettleAccounts);
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
     * @param agencySettleAccountsVO
     * @return
     */
    @Override
    @Transactional
    public Result<AgencySettleAccounts> remit(AgencySettleAccountsVO agencySettleAccountsVO) {
        Result<AgencySettleAccounts> result = new Result<>();
        AgencySettleAccounts agencySettleAccounts = this.getById(agencySettleAccountsVO.getId());
        BeanUtils.copyProperties(agencySettleAccountsVO,agencySettleAccounts);
        if (agencySettleAccountsVO.getStatus().equals("2")){
            //查出代理余额记录
            AgencyRechargeRecord rechargeRecord = iAgencyRechargeRecordService.getOne(new LambdaQueryWrapper<AgencyRechargeRecord>()
                    .eq(AgencyRechargeRecord::getTradeNo,agencySettleAccounts.getOrderNo())
                    .eq(AgencyRechargeRecord::getSysUserId,agencySettleAccounts.getSysUserId())
                    .eq(AgencyRechargeRecord::getGoAndCome,"1")
                    .eq(AgencyRechargeRecord::getTradeStatus,"3")
                    .eq(AgencyRechargeRecord::getPayType,"1"));
            if (oConvertUtils.isEmpty(rechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //交易成功写入余额记录
            iAgencyRechargeRecordService.updateById(rechargeRecord
                    .setTradeStatus("5"));

            AgencyManage agencyManage = iAgencyManageService.getOne(new LambdaQueryWrapper<AgencyManage>()
                    .eq(AgencyManage::getSysUserId, agencySettleAccounts.getSysUserId()));
            //交易成功扣除不可用金额
            iAgencyManageService.updateById(agencyManage
                    .setUnusableFrozen(agencyManage.getUnusableFrozen().subtract(rechargeRecord.getAmount())));

            //交易成功形成代理资金流水记录
            AgencyAccountCapital agencyAccountCapital = new AgencyAccountCapital();
            agencyAccountCapital.setDelFlag("0");
            agencyAccountCapital.setSysUserId(agencySettleAccounts.getSysUserId());
            agencyAccountCapital.setPayType("1");
            agencyAccountCapital.setGoAndCome("1");
            agencyAccountCapital.setAmount(agencySettleAccounts.getAmount());
            agencyAccountCapital.setOrderNo(rechargeRecord.getOrderNo());
            agencyAccountCapital.setBalance(agencyManage.getBalance());
            iAgencyAccountCapitalService.save(agencyAccountCapital);
            agencySettleAccounts.setPayTime(new Date());

        }else if (agencySettleAccountsVO.getStatus().equals("1")){
            result.setMessage("操作成功!");
        }else {
            return result.error500("操作异常,请重试!");
        }

        boolean b = this.updateById(agencySettleAccounts);
        if (b){
            result.setCode(200);
            result.setMessage("操作成功!");
        }else {
            result.error500("操作失败!");
        }
        return result;
    };


}
