package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.dto.StoreWithdrawDepositDTO;
import org.jeecg.modules.store.entity.StoreWithdrawDeposit;
import org.jeecg.modules.store.mapper.StoreWithdrawDepositMapper;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreWithdrawDepositService;
import org.jeecg.modules.store.vo.StoreWithdrawDepositVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @Description: 店铺提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
@Service
public class StoreWithdrawDepositServiceImpl extends ServiceImpl<StoreWithdrawDepositMapper, StoreWithdrawDeposit> implements IStoreWithdrawDepositService {
  @Autowired(required = false)
  private StoreWithdrawDepositMapper storeWithdrawDepositMapper;
  @Autowired
  @Lazy
  private IStoreRechargeRecordService iStoreRechargeRecordService;
  @Autowired
  private IStoreAccountCapitalService iStoreAccountCapitalService;
  @Autowired
  private IStoreManageService iStoreManageService;
   public IPage<StoreWithdrawDepositDTO> getStoreWithdrawDeposit(Page<StoreWithdrawDeposit> page, StoreWithdrawDepositVO storeWithdrawDepositVO){
      return  storeWithdrawDepositMapper.getStoreWithdrawDeposit(page,storeWithdrawDepositVO);
   }

    /**
     * 提现审核
     * @param storeWithdrawDepositVO
     * @return
     */
    @Override
    @Transactional
    public Result<StoreWithdrawDeposit> audit(StoreWithdrawDepositVO storeWithdrawDepositVO) {
        Result<StoreWithdrawDeposit> result = new Result<>();
        StoreWithdrawDeposit storeWithdrawDeposit = this.getById(storeWithdrawDepositVO.getId());
        storeWithdrawDeposit.setStatus(storeWithdrawDepositVO.getStatus());
        storeWithdrawDeposit.setRemark(storeWithdrawDepositVO.getRemark());
        storeWithdrawDeposit.setCloseExplain(storeWithdrawDepositVO.getCloseExplain());
        storeWithdrawDeposit.setAuditTime(new Date());
        if (storeWithdrawDepositVO.getStatus().equals("1")){
            /*//查出店铺余额记录
            StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getOne(new LambdaQueryWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo,storeWithdrawDepositVO.getOrderNo())
                    .eq(StoreRechargeRecord::getStoreManageId,storeWithdrawDepositVO.getStoreManageId())
                    .eq(StoreRechargeRecord::getGoAndCome,"1")
                    .eq(StoreRechargeRecord::getPayType,"1")
                    .eq(StoreRechargeRecord::getTradeStatus,"1"));
            if (oConvertUtils.isEmpty(storeRechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //修改店铺余额记录
            iStoreRechargeRecordService.updateById(storeRechargeRecord
                    .setTradeStatus("3"));*/
        }else if (storeWithdrawDepositVO.getStatus().equals("3")){
            /*//查出店铺余额记录
            StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getOne(new LambdaQueryWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo,storeWithdrawDepositVO.getOrderNo())
                    .eq(StoreRechargeRecord::getStoreManageId,storeWithdrawDepositVO.getStoreManageId())
                    .eq(StoreRechargeRecord::getGoAndCome,"1")
                    .eq(StoreRechargeRecord::getPayType,"1")
                    .eq(StoreRechargeRecord::getTradeStatus,"1"));
            if (oConvertUtils.isEmpty(storeRechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //修改店铺余额记录
            iStoreRechargeRecordService.updateById(storeRechargeRecord.setTradeStatus("6"));
            //查出提现店铺
            StoreManage storeManage = iStoreManageService.getById(storeWithdrawDepositVO.getStoreManageId());
            //退还店铺提现金额
            storeManage.setBalance(storeManage.getBalance().add(storeRechargeRecord.getAmount()));
            //扣除店铺不可用金额
            storeManage.setUnusableFrozen(storeManage.getUnusableFrozen().subtract(storeRechargeRecord.getAmount()));
            iStoreManageService.updateById(storeManage);*/
            iStoreManageService.addStoreBlance(storeWithdrawDeposit.getStoreManageId(),storeWithdrawDeposit.getMoney(),storeWithdrawDeposit.getOrderNo(),"14");
        }else {
            return result.error500("操作异常,请重试!");
        }
        //修改状态
        boolean b = this.updateById(storeWithdrawDeposit);
        if (b){
            result.setCode(200);
            result.setMessage("操作成功!");
        }else {
            result.error500("操作失败!");
        }
        return result;
    }

    /**
     * 提现打款
     * @param storeWithdrawDepositVO
     * @return
     */
    @Override
    @Transactional
    public Result<StoreWithdrawDeposit> remit(StoreWithdrawDepositVO storeWithdrawDepositVO) {
        Result<StoreWithdrawDeposit> result = new Result<>();
        StoreWithdrawDeposit storeWithdrawDeposit = this.getById(storeWithdrawDepositVO.getId());
        if (storeWithdrawDepositVO.getStatus().equals("2")){
            /*//查出店铺余额记录
            StoreRechargeRecord storeRechargeRecord = iStoreRechargeRecordService.getOne(new LambdaQueryWrapper<StoreRechargeRecord>()
                    .eq(StoreRechargeRecord::getTradeNo,storeWithdrawDepositVO.getOrderNo())
                    .eq(StoreRechargeRecord::getStoreManageId,storeWithdrawDepositVO.getStoreManageId())
                    .eq(StoreRechargeRecord::getGoAndCome,"1")
                    .eq(StoreRechargeRecord::getPayType,"1")
                    .eq(StoreRechargeRecord::getTradeStatus,"3"));
            if (oConvertUtils.isEmpty(storeRechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //交易成功写入余额记录
            iStoreRechargeRecordService.updateById(storeRechargeRecord
                    .setTradeStatus("5"));
            StoreManage storeManage = iStoreManageService.getById(storeRechargeRecord.getStoreManageId());
            //扣除店铺不可用金额
            storeManage.setUnusableFrozen(storeManage.getUnusableFrozen().subtract(storeRechargeRecord.getAmount()));
            iStoreManageService.updateById(storeManage);

            //交易成功形成店铺资金流水记录
            StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
            storeAccountCapital.setDelFlag("0");
            storeAccountCapital.setStoreManageId(storeRechargeRecord.getStoreManageId());
            storeAccountCapital.setPayType("1");
            storeAccountCapital.setGoAndCome("1");
            storeAccountCapital.setAmount(storeRechargeRecord.getAmount());
            storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
            storeAccountCapital.setBalance(storeManage.getBalance());
            iStoreAccountCapitalService.save(storeAccountCapital);*/
            storeWithdrawDeposit.setPayTime(new Date());
            storeWithdrawDeposit.setStatus("2");
        }else if (storeWithdrawDepositVO.getStatus().equals("1")){
            result.setMessage("操作成功!");
        }else {
            return result.error500("操作异常,请重试!");
        }
        boolean b = this.updateById(storeWithdrawDeposit);
        if (b){
            result.setCode(200);
            result.setMessage("操作成功!");
        }else {
            result.error500("操作失败!");
        }
        return result;
    };
    /**
     * 商家端提现明细
     * @param page
     * @param storeWithdrawDepositVO
     * @return
     */
    @Override
    public IPage<Map<String,Object >> getStoreWithdrawDepositMap(Page<StoreWithdrawDeposit> page, StoreWithdrawDepositVO storeWithdrawDepositVO){
      return  baseMapper.getStoreWithdrawDepositMap(page,storeWithdrawDepositVO);
    };

}
