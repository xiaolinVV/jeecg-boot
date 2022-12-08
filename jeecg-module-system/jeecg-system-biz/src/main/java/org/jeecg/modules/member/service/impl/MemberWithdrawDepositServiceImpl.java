package org.jeecg.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.entity.MemberWithdrawDeposit;
import org.jeecg.modules.member.mapper.MemberWithdrawDepositMapper;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberWithdrawDepositVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 会员提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Service
@Log
public class MemberWithdrawDepositServiceImpl extends ServiceImpl<MemberWithdrawDepositMapper, MemberWithdrawDeposit> implements IMemberWithdrawDepositService {

    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;
    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;
    @Autowired
    private IMemberBankCardService iMemberBankCardService;

    @Override
    public IPage<MemberWithdrawDepositVO> getMemberWithdrawDeposit(Page<MemberWithdrawDeposit> page, MemberWithdrawDepositVO memberWithdrawDepositVO){
     return   baseMapper.getMemberWithdrawDeposit(page,memberWithdrawDepositVO)  ;
    }


    @Override
    public IPage<Map<String, Object>> findMemberWithdrawDepositByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMemberWithdrawDepositByMemberId(page,paramMap);
    }

    @Override
    public Map<String, Object> findMemberWithdrawDepositById(String id) {
        return baseMapper.findMemberWithdrawDepositById(id);
    }

    /**
     * 审核
     * @param memberWithdrawDepositVO
     * @return
     */
    @Override
    @Transactional
    public Result<MemberWithdrawDeposit> audit(MemberWithdrawDepositVO memberWithdrawDepositVO) {
        Result<MemberWithdrawDeposit> result = new Result<>();
        MemberWithdrawDeposit memberWithdrawDeposit = this.getById(memberWithdrawDepositVO.getId());
        memberWithdrawDeposit.setAuditTime(new Date());
        memberWithdrawDeposit.setStatus(memberWithdrawDepositVO.getStatus());
        memberWithdrawDeposit.setRemark(memberWithdrawDepositVO.getRemark());
        memberWithdrawDeposit.setCloseExplain(memberWithdrawDepositVO.getCloseExplain());
        if (memberWithdrawDepositVO.getStatus().equals("1")){
            //查出会员余额记录
//            MemberRechargeRecord memberRechargeRecord = iMemberRechargeRecordService.getOne(new LambdaQueryWrapper<MemberRechargeRecord>()
//                    .eq(MemberRechargeRecord::getTradeNo,memberWithdrawDeposit.getOrderNo())
//                    .eq(MemberRechargeRecord::getMemberListId,memberWithdrawDeposit.getMemberListId()));
//            if (oConvertUtils.isEmpty(memberRechargeRecord)){
//                return result.error500("状态异常,请联系管理员!");
//            }
//            //修改会员余额记录
//            memberRechargeRecord.setTradeStatus("5");
//            iMemberRechargeRecordService.updateById(memberRechargeRecord);
            JSONArray jsonArray = JSON.parseArray(memberWithdrawDeposit.getProcessInformation());
            JSONObject object = new JSONObject();
            object.put("time",DateUtils.formatDateTime());
            if (StringUtils.isNotBlank(memberWithdrawDepositVO.getCloseExplain())){
                object.put("statusExplain","审核人员: "+memberWithdrawDepositVO.getRealname()+"; 审核：通过; 审核说明: "+memberWithdrawDepositVO.getCloseExplain());
            }else {
                object.put("statusExplain","审核人员: "+memberWithdrawDepositVO.getRealname()+"; 审核：通过; 审核说明: 无");
            }

            object.put("status","打款处理");
            jsonArray.add(object);
            memberWithdrawDeposit.setProcessInformation(jsonArray.toJSONString());
        }else if (memberWithdrawDepositVO.getStatus().equals("3")){
            /*//查出会员余额记录
            MemberRechargeRecord oldMemberRechargeRecord = iMemberRechargeRecordService.getOne(new LambdaQueryWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo,memberWithdrawDeposit.getOrderNo())
                    .eq(MemberRechargeRecord::getMemberListId,memberWithdrawDeposit.getMemberListId()));
            if (oConvertUtils.isEmpty(oldMemberRechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }*/
            boolean b = iMemberListService.addBlance(memberWithdrawDeposit.getMemberListId(), memberWithdrawDeposit.getMoney(), memberWithdrawDeposit.getOrderNo(), "11");
            if (b){
                JSONArray jsonArray = JSON.parseArray(memberWithdrawDeposit.getProcessInformation());
                JSONObject object = new JSONObject();
                object.put("time",DateUtils.formatDateTime());
                if (StringUtils.isNotBlank(memberWithdrawDepositVO.getCloseExplain())){
                    object.put("statusExplain","审核人员: "+memberWithdrawDepositVO.getRealname()+"; 审核：未通过; 审核说明: "+memberWithdrawDepositVO.getCloseExplain());
                }else {
                    object.put("statusExplain","审核人员: "+memberWithdrawDepositVO.getRealname()+"; 审核：未通过; 审核说明: 无");
                }

                object.put("status","打款处理");
                jsonArray.add(object);
                memberWithdrawDeposit.setProcessInformation(jsonArray.toJSONString());
            }else {
                return result.error500("操作失败!请重试");
            }
        }else {
            result.error500("操作失败!请重试.");
        }
        //修改状态
        boolean b = this.updateById(memberWithdrawDeposit);
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
     * @param memberWithdrawDepositVO
     * @return
     */
    @Override
    @Transactional
    public Result<String> remit(MemberWithdrawDepositVO memberWithdrawDepositVO) {
        Result<String> result = new Result<>();
        MemberWithdrawDeposit memberWithdrawDeposit = this.getById(memberWithdrawDepositVO.getId());

        if (memberWithdrawDepositVO.getStatus().equals("2")){
            //查出会员余额记录
            /*MemberRechargeRecord memberRechargeRecord = iMemberRechargeRecordService.getOne(new LambdaQueryWrapper<MemberRechargeRecord>()
                    .eq(MemberRechargeRecord::getTradeNo,memberWithdrawDeposit.getOrderNo())
                    .eq(MemberRechargeRecord::getMemberListId,memberWithdrawDeposit.getMemberListId()));
            if (oConvertUtils.isEmpty(memberRechargeRecord)){
                return result.error500("状态异常,请联系管理员!");
            }
            //交易成功写入余额记录
            memberRechargeRecord.setTradeStatus("5");
            iMemberRechargeRecordService.saveOrUpdate(memberRechargeRecord);*/

            /*MemberList memberList = iMemberListService.getById(memberWithdrawDeposit.getMemberListId());

            iMemberListService.saveOrUpdate(memberList
                    .setHaveWithdrawal(memberList.getHaveWithdrawal().add(memberWithdrawDeposit.getAmount())));*/
            //交易成功形成会员资金流水记录
            /*MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
            memberAccountCapital.setDelFlag("0");
            memberAccountCapital.setMemberListId(memberList.getId());
            memberAccountCapital.setPayType("1");
            memberAccountCapital.setGoAndCome("1");
            memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
            memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
            memberAccountCapital.setBalance(memberList.getBalance());
            iMemberAccountCapitalService.save(memberAccountCapital);*/
            JSONArray jsonArray = JSON.parseArray(memberWithdrawDeposit.getProcessInformation());
            JSONObject object = new JSONObject();
            object.put("time",DateUtils.formatDateTime());
            object.put("statusExplain","打款人员: "+memberWithdrawDepositVO.getRealname()+"; 打款说明："+memberWithdrawDepositVO.getRemark()+";打款凭证"+memberWithdrawDepositVO.getReceiptVoucher());
            object.put("status","打款处理");
            jsonArray.add(object);
            this.saveOrUpdate(memberWithdrawDeposit
                    .setStatus("2")
                    .setPayTime(new Date())
                    .setReceiptVoucher(memberWithdrawDepositVO.getReceiptVoucher())
                    .setProcessInformation(jsonArray.toJSONString())
                    .setRemark(memberWithdrawDepositVO.getRemark()));
        }else {
            result.setMessage("操作成功!");
        }
        return result;
    }
    //小程序用户提现(微信申请)
    @Override
    @Transactional
    public String addWithdrawDeposit(String memberId,BigDecimal money) {
        MemberList memberList=iMemberListService.getById(memberId);
        //判断提现金额不为零
        if(memberList.getBalance().doubleValue()<=0){
            return "用户可提现金额为零不可提现";
        }
        //扣除余额  2021年4月14日15:55:34需求变更 :启用不可用金额
        /*iMemberListService.updateById(memberList
                .setBalance(memberList.getBalance().subtract(money)));*/
        //获取常量信息
        String withdrawalServiceCharge= StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdrawal_service_charge"),"%");

        //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_threshold");

        //生成流水号
        MemberWithdrawDeposit memberWithdrawDeposit = new MemberWithdrawDeposit()
                .setDelFlag("0")
                .setMemberListId(memberList.getId())
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setPhone(memberList.getPhone())
                .setMoney(money)
                .setWithdrawalType("0")
                .setTimeApplication(new Date())
                .setStatus("0")
                .setBankCard(memberList.getPhone())
                .setBankName("微信")
                .setCardholder(memberList.getNickName());
        if (StringUtils.isNotBlank(withdrawThreshold)){

            //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
            String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_type");
            //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
            String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_type");
            //小于阈值固定费用（fixed）
            String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_fixed");
            //小于阈值百分比（percent）
            String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_percent");
            //大于阈值固定费用（fixed）
            String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_fixed");
            //大于阈值百分比（percent）
            String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_percent");
            if (money.doubleValue()<=Double.valueOf(withdrawThreshold)){
                if (withdrawServiceChargeLessType.equals("0")){
                    memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeLessFixed));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }else {
                    memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }
            }else {
                if (withdrawServiceChargeGreaterType.equals("0")){
                    memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeGreaterFixed));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }else {
                    memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }
            }
        }else {
            memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawalServiceCharge).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
            memberWithdrawDeposit.setAmount(money.subtract(money.multiply(new BigDecimal(withdrawalServiceCharge).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN)));
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("time",DateUtils.formatDateTime());
        object.put("statusExplain","用户提交提现申请");
        object.put("status","提现申请");
        jsonArray.add(object);
        memberWithdrawDeposit.setProcessInformation(JSON.toJSONString(jsonArray));

        boolean b = iMemberListService.subtractBlance(memberList.getId(), money, memberWithdrawDeposit.getOrderNo(), "1");
        if (b){
            this.save(memberWithdrawDeposit);
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        /*MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                .setDelFlag("0")
                .setMemberListId(memberList.getId())
                .setPayType("1")
                .setGoAndCome("1")
                .setAmount(money)
                .setTradeStatus("5")
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setOperator(memberList.getNickName())
                .setRemark("余额提现["+memberWithdrawDeposit.getOrderNo()+"]")
                .setTradeNo(memberWithdrawDeposit.getOrderNo())
                .setMemberWithdrawDepositId(memberWithdrawDeposit.getId())
                .setTMemberListId(memberList.getPromoter());
        iMemberRechargeRecordService.save(memberRechargeRecord);
        //交易成功形成会员资金流水记录
        MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
        memberAccountCapital.setDelFlag("0");
        memberAccountCapital.setMemberListId(memberList.getId());
        memberAccountCapital.setPayType("1");
        memberAccountCapital.setGoAndCome("1");
        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
        memberAccountCapital.setBalance(memberList.getBalance());
        iMemberAccountCapitalService.save(memberAccountCapital);*/
        if (b){
            return "SUCCESS";
        }else {
            return "err";
        }

    }



    /**
     * 微信提现后(数据更改)
     * @param memberList
     * @param memberRechargeRecord
     * @param memberWithdrawDeposit
     */
    @Override
    public void weChatWithdrawal(MemberList memberList,MemberRechargeRecord memberRechargeRecord,MemberWithdrawDeposit memberWithdrawDeposit){

        //交易成功写入余额记录
        memberRechargeRecord.setTradeStatus("5");
        iMemberRechargeRecordService.saveOrUpdate(memberRechargeRecord);

        iMemberListService.saveOrUpdate(memberList
                .setHaveWithdrawal(memberList.getHaveWithdrawal().add(memberRechargeRecord.getAmount())));
        /*//交易成功形成会员资金流水记录
        MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
        memberAccountCapital.setDelFlag("0");
        memberAccountCapital.setMemberListId(memberList.getId());
        memberAccountCapital.setPayType("1");
        memberAccountCapital.setGoAndCome("1");
        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
        memberAccountCapital.setBalance(memberList.getBalance());
        iMemberAccountCapitalService.save(memberAccountCapital);*/

        this.updateById(memberWithdrawDeposit
                .setPayTime(new Date())
                .setStatus("2"));

    }

    /**
     * 微信(银行卡)提现后(数据更改)后台
     * @param memberList
     * @param memberRechargeRecord
     * @param memberWithdrawDeposit
     */
    @Override
    public void  postWXPayToBC(MemberList memberList,MemberRechargeRecord memberRechargeRecord,MemberWithdrawDeposit memberWithdrawDeposit){


        //交易成功写入余额记录
        memberRechargeRecord.setTradeStatus("5");
        iMemberRechargeRecordService.saveOrUpdate(memberRechargeRecord);
        iMemberListService.saveOrUpdate(memberList
                .setHaveWithdrawal(memberList.getHaveWithdrawal().add(memberRechargeRecord.getAmount())));
        //交易成功形成会员资金流水记录
        /*MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
        memberAccountCapital.setDelFlag("0");
        memberAccountCapital.setMemberListId(memberList.getId());
        memberAccountCapital.setPayType("1");
        memberAccountCapital.setGoAndCome("1");
        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
        memberAccountCapital.setBalance(memberList.getBalance());
        iMemberAccountCapitalService.save(memberAccountCapital);*/
        this.updateById(memberWithdrawDeposit
                .setPayTime(new Date())
                .setStatus("2")
        );

    }

    @Override
    @Transactional
    public boolean memberWithdrawDepositAdd(BigDecimal money, MemberList member, MemberBankCard bankCard) {
        //扣除余额  2021年4月14日15:55:34需求变更 :启用不可用金额
        /*iMemberListService.updateById(member
                .setBalance(member.getBalance().subtract(money)));*/

        //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_threshold");
        //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_type");
        //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_type");
        //小于阈值固定费用（fixed）
        String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_fixed");
        //小于阈值百分比（percent）
        String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_percent");
        //大于阈值固定费用（fixed）
        String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_fixed");
        //大于阈值百分比（percent）
        String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_percent");

        //生成流水号
        MemberWithdrawDeposit memberWithdrawDeposit = new MemberWithdrawDeposit()
                .setDelFlag("0")
                .setMemberListId(member.getId())
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setPhone(member.getPhone())
                .setMoney(money)
                .setWithdrawalType("2")
                .setTimeApplication(new Date())
                .setStatus("0")
                .setRemark("提现到银行卡")
                .setBankCard(bankCard.getBankCard())
                .setBankName(bankCard.getBankName())
                .setCardholder(bankCard.getCardholder())
                .setOpeningBank(bankCard.getOpeningBank())
                .setIdentityNumber(bankCard.getIdentityNumber());
        if (money.doubleValue()<=Double.valueOf(withdrawThreshold)){
            if (withdrawServiceChargeLessType.equals("0")){
                memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeLessFixed));
                memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
            }else {
                memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
            }
        }else {
            if (withdrawServiceChargeGreaterType.equals("0")){
                memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeGreaterFixed));
                memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
            }else {
                memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
            }
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("time",DateUtils.formatDateTime());
        object.put("statusExplain","用户提交提现申请");
        object.put("status","提现申请");
        jsonArray.add(object);
        memberWithdrawDeposit.setProcessInformation(jsonArray.toJSONString());
        boolean b = iMemberListService.subtractBlance(member.getId(), money, memberWithdrawDeposit.getOrderNo(), "1");
        if (b){
            this.save(memberWithdrawDeposit);
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return b;

        /*MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                .setDelFlag("0")
                .setMemberListId(member.getId())
                .setPayType("1")
                .setGoAndCome("1")
                .setAmount(money)
                .setTradeStatus("5")
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setMemberBankCardId(bankCard.getId())
                .setOperator(member.getNickName())
                .setRemark("余额提现["+memberWithdrawDeposit.getOrderNo()+"]")
                .setTradeNo(memberWithdrawDeposit.getOrderNo())
                .setMemberWithdrawDepositId(memberWithdrawDeposit.getId())
                .setTMemberListId(member.getPromoter());
        iMemberRechargeRecordService.save(memberRechargeRecord);
        //交易成功形成会员资金流水记录
        MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
        memberAccountCapital.setDelFlag("0");
        memberAccountCapital.setMemberListId(member.getId());
        memberAccountCapital.setPayType("1");
        memberAccountCapital.setGoAndCome("1");
        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
        memberAccountCapital.setBalance(member.getBalance());
        iMemberAccountCapitalService.save(memberAccountCapital);*/
    }

    //小程序用户提现(银行卡)
    @Override
    @Transactional
    public boolean withdrawalCard(BigDecimal money,MemberList member, MemberBankCard bankCard) {
        //扣除余额  2021年4月14日15:55:34需求变更 :启用不可用金额
        /*iMemberListService.updateById(member
                .setBalance(member.getBalance().subtract(money)));*/
            //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
            String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_threshold");

            //获取字典配置提现手续费比例
            String withdrawalServiceCharge= StringUtils.substringBefore(iSysDictService
                    .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdrawal_service_charge"),"%");

            //生成流水号
            MemberWithdrawDeposit memberWithdrawDeposit = new MemberWithdrawDeposit()
                    .setDelFlag("0")
                    .setMemberListId(member.getId())
                    .setOrderNo(OrderNoUtils.getOrderNo())
                    .setPhone(member.getPhone())
                    .setMoney(money)
                    .setWithdrawalType("2")
                    .setTimeApplication(new Date())
                    .setStatus("0")
                    .setRemark("提现到银行卡")
                    .setBankCard(bankCard.getBankCard())
                    .setBankName(bankCard.getBankName())
                    .setCardholder(bankCard.getCardholder())
                    .setOpeningBank(bankCard.getOpeningBank())
                    .setIdentityNumber(bankCard.getIdentityNumber());
        if (StringUtils.isNotBlank(withdrawThreshold)){
            //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
            String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_type");
            //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
            String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_type");
            //小于阈值固定费用（fixed）
            String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_fixed");
            //小于阈值百分比（percent）
            String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_percent");
            //大于阈值固定费用（fixed）
            String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_fixed");
            //大于阈值百分比（percent）
            String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_percent");
            if (money.doubleValue()<=Double.valueOf(withdrawThreshold)){
                if (withdrawServiceChargeLessType.equals("0")){
                    memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeLessFixed));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }else {
                    memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }
            }else {
                if (withdrawServiceChargeGreaterType.equals("0")){
                    memberWithdrawDeposit.setServiceCharge(new BigDecimal(withdrawServiceChargeGreaterFixed));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }else {
                    memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    memberWithdrawDeposit.setAmount(money.subtract(memberWithdrawDeposit.getServiceCharge()));
                }
            }
        }else {
            memberWithdrawDeposit.setServiceCharge(money.multiply(new BigDecimal(withdrawalServiceCharge).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
            memberWithdrawDeposit.setAmount(money.subtract(money.multiply(new BigDecimal(withdrawalServiceCharge).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN)));
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("time",DateUtils.formatDateTime());
        object.put("statusExplain","用户提交提现申请");
        object.put("status","提现申请");
        jsonArray.add(object);
        memberWithdrawDeposit.setProcessInformation(JSON.toJSONString(jsonArray));
        boolean b = iMemberListService.subtractBlance(member.getId(), memberWithdrawDeposit.getMoney(), memberWithdrawDeposit.getOrderNo(), "1");
        if (b) {
            this.save(memberWithdrawDeposit);
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
            /*MemberRechargeRecord memberRechargeRecord = new MemberRechargeRecord()
                    .setDelFlag("0")
                    .setMemberListId(member.getId())
                    .setPayType("1")
                    .setGoAndCome("1")
                    .setAmount(money)
                    .setTradeStatus("5")
                    .setOrderNo(OrderNoUtils.getOrderNo())
                    .setMemberBankCardId(bankCard.getId())
                    .setOperator(member.getNickName())
                    .setRemark("余额提现["+memberWithdrawDeposit.getOrderNo()+"]")
                    .setTradeNo(memberWithdrawDeposit.getOrderNo())
                    .setMemberWithdrawDepositId(memberWithdrawDeposit.getId())
                    .setTMemberListId(member.getPromoter());
        iMemberRechargeRecordService.save(memberRechargeRecord);
        //交易成功形成会员资金流水记录
        MemberAccountCapital memberAccountCapital = new MemberAccountCapital();
        memberAccountCapital.setDelFlag("0");
        memberAccountCapital.setMemberListId(member.getId());
        memberAccountCapital.setPayType("1");
        memberAccountCapital.setGoAndCome("1");
        memberAccountCapital.setAmount(memberRechargeRecord.getAmount());
        memberAccountCapital.setOrderNo(memberRechargeRecord.getOrderNo());
        memberAccountCapital.setBalance(member.getBalance());
        iMemberAccountCapitalService.save(memberAccountCapital);*/
            return b;
    }

    @Override
    public IPage<Map<String, Object>> findMemberWithdrawDepositPageByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramObjectMap) {
        return baseMapper.findMemberWithdrawDepositPageByMemberId(page,paramObjectMap);
    }

    @Override
    public Map<String, Object> findMemberWithdrawDepositParticulars(String id) {
        return baseMapper.findMemberWithdrawDepositParticulars(id);
    }

}
