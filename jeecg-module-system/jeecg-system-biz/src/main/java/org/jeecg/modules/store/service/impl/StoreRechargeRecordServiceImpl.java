package org.jeecg.modules.store.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.dto.StoreRechargeRecordDTO;
import org.jeecg.modules.store.entity.*;
import org.jeecg.modules.store.mapper.StoreRechargeRecordMapper;
import org.jeecg.modules.store.service.IStoreBankCardService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreWithdrawDepositService;
import org.jeecg.modules.store.vo.StoreRechargeRecordVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 店铺余额记录
 * @Author: jeecg-boot
 * @Date: 2019-12-11
 * @Version: V1.0
 */
@Service
public class StoreRechargeRecordServiceImpl extends ServiceImpl<StoreRechargeRecordMapper, StoreRechargeRecord> implements IStoreRechargeRecordService {
    @Autowired(required = false)
    private StoreRechargeRecordMapper storeRechargeRecordMapper;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IStoreBankCardService iStoreBankCardService;
    @Autowired
    @Lazy
    private IStoreWithdrawDepositService iStoreWithdrawDepositService;

    @Override
    public IPage<StoreRechargeRecordDTO> getStoreRechargeRecord(Page<StoreRechargeRecord> page, StoreRechargeRecordVO storeRechargeRecordVO) {
        return storeRechargeRecordMapper.getStoreRechargeRecord(page, storeRechargeRecordVO);
    }

    /**
     * 店铺提现
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional
    public Result<StoreRechargeRecordDTO> cashOut(JSONObject jsonObject) {
        Result<StoreRechargeRecordDTO> result = new Result<>();
        String id = jsonObject.getString("id");//店铺id
        BigDecimal amount = jsonObject.getBigDecimal("amount");//交易金额
        String storeBankCardId = jsonObject.getString("storeBankCardId");//银行卡id
        String remark = jsonObject.getString("remark");//备注
        if (StringUtils.isBlank(storeBankCardId)){
            return result.error500("请先设置银行卡");
        }
        //获取店铺
        StoreManage store = iStoreManageService.getById(id);
        if (oConvertUtils.isEmpty(store)){
            return result.error500("店铺信息异常,请重新操作");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        StoreBankCard storeBankCard = iStoreBankCardService.getById(storeBankCardId);
        if (oConvertUtils.isEmpty(storeBankCard)){
            return result.error500("银行卡信息异常,请重新设置银行卡信息");
        }
        //获取手续费比例
        String s = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "withdrawal_service_charge"), "%");
        //判断店铺余额
        if (amount.doubleValue() > store.getBalance().doubleValue()){
            return result.error500("店铺余额不足");
        }
        /*//减去店铺余额2021年7月16日15:52:47废弃
        store.setBalance(store.getBalance().subtract(amount));
        //添加到不可用金额
        store.setUnusableFrozen(store.getUnusableFrozen().add(amount));
        iStoreManageService.updateById(store);*/
        StoreWithdrawDeposit storeWithdrawDeposit = new StoreWithdrawDeposit();
        storeWithdrawDeposit.setDelFlag("0");//删除状态
        storeWithdrawDeposit.setStoreManageId(id);//店铺id
        storeWithdrawDeposit.setOrderNo(OrderNoUtils.getOrderNo());//单号
        storeWithdrawDeposit.setPhone(store.getBossPhone());//手机号
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_threshold");
        //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_type");
        //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_type");
        //小于阈值固定费用（fixed）
        String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_fixed");
        //小于阈值百分比（percent）
        String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_less_percent");
        //大于阈值固定费用（fixed）
        String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_fixed");
        //大于阈值百分比（percent）
        String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "store_withdraw_service_charge_greater_percent");
        //手续费
//        BigDecimal c = amount.multiply(new BigDecimal(s).divide(new BigDecimal(100)));
        storeWithdrawDeposit.setMoney(amount);//提现金额
        storeWithdrawDeposit.setWithdrawalType("2");//提现类型(银行卡)
        /*if (storeBankCard.getCarType().equals("0")){
            storeWithdrawDeposit.setWithdrawalType("2");//提现类型(银行卡)
        }else if (storeBankCard.getCarType().equals("1")){
            storeWithdrawDeposit.setWithdrawalType("1");//提现类型(支付宝)
        }else {
            storeWithdrawDeposit.setWithdrawalType("0");//提现类型(微信)
        }*/
        if (amount.doubleValue()<=Double.valueOf(withdrawThreshold)){
            if (withdrawServiceChargeLessType.equals("0")){
                storeWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeLessFixed));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }else {
                storeWithdrawDeposit.setServiceCharge(amount.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }
        }else {
            if (withdrawServiceChargeGreaterType.equals("0")){
                storeWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeGreaterFixed));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }else {
                storeWithdrawDeposit.setServiceCharge(amount.multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                storeWithdrawDeposit.setAmount(amount.subtract(storeWithdrawDeposit.getServiceCharge()));
            }
        }
        storeWithdrawDeposit.setTimeApplication(new Date());//申请时间
        storeWithdrawDeposit.setStatus("0");//状态
        storeWithdrawDeposit.setRemark(remark);//备注
        storeWithdrawDeposit.setBankCard(storeBankCard.getBankCard());//银行卡号(支付宝账号)
        storeWithdrawDeposit.setBankName(storeBankCard.getBankName());//开户行名称
        storeWithdrawDeposit.setCardholder(storeBankCard.getCardholder());//持卡人姓名(真实姓名)
        storeWithdrawDeposit.setBossPhone(store.getBossPhone());//老板电话
        storeWithdrawDeposit.setOpeningBank(storeBankCard.getOpeningBank());
        iStoreWithdrawDepositService.save(storeWithdrawDeposit);

        /*StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
        storeRechargeRecord.setDelFlag("0");//删除状态
        storeRechargeRecord.setStoreManageId(id);//店铺id
        storeRechargeRecord.setPayType("1");//交易类型
        storeRechargeRecord.setGoAndCome("1");//支付和收入；0：收入；1：支出
        storeRechargeRecord.setAmount(amount);//交易金额
        storeRechargeRecord.setTradeStatus("1");//交易状态
        storeRechargeRecord.setOrderNo(OrderNoUtils.getOrderNo());//单号
        storeRechargeRecord.setStoreBankCardId(storeBankCardId);//银行卡id
        storeRechargeRecord.setOperator(sysUser.getUsername());//操作人
        storeRechargeRecord.setRemark("余额提现["+storeWithdrawDeposit.getOrderNo()+"]");//备注
        storeRechargeRecord.setTradeNo(storeWithdrawDeposit.getOrderNo());//交易单号
        storeRechargeRecord.setBankCard(storeBankCard.getBankCard());//银行卡号(支付宝账号)
        storeRechargeRecord.setBankName(storeBankCard.getBankName());//开户行名称
        storeRechargeRecord.setCardholder(storeBankCard.getCardholder());//持卡人姓名(真实姓名)

        boolean b = this.save(storeRechargeRecord);*/
        boolean b = iStoreManageService.subtractStoreBlance(store.getId(), amount, storeWithdrawDeposit.getOrderNo(), "1");
        if (b) {
            result.setCode(200);
            result.setMessage("操作成功,待审核!");
        } else {
            result.error500("操作失败!");
        }
        return result;
    }

    @Override
    public IPage<Map<String, Object>> findStoreRechargeRecordInfo(Page<StoreAccountCapital> page, HashMap<String, Object> map) {
        return baseMapper.findStoreRechargeRecordInfo(page,map);
    }

    @Override
    public IPage<StoreRechargeRecordVO> queryPageList(Page<StoreRechargeRecord> page, StoreRechargeRecordDTO storeRechargeRecordDTO) {
        return baseMapper.queryPageList(page,storeRechargeRecordDTO);
    }

    @Override
    public IPage<StoreRechargeRecordVO> findRechargeRecord(Page<StoreRechargeRecord> page, StoreRechargeRecordDTO storeRechargeRecordDTO) {
        return baseMapper.findRechargeRecord(page,storeRechargeRecordDTO);
    }

    ;

}
