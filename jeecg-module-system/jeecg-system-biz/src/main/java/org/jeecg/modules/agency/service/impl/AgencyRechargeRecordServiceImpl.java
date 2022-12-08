package org.jeecg.modules.agency.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.dto.AgencyRechargeRecordDTO;
import org.jeecg.modules.agency.entity.AgencyBankCard;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.entity.AgencySettleAccounts;
import org.jeecg.modules.agency.mapper.AgencyRechargeRecordMapper;
import org.jeecg.modules.agency.service.IAgencyBankCardService;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.service.IAgencyRechargeRecordService;
import org.jeecg.modules.agency.service.IAgencySettleAccountsService;
import org.jeecg.modules.agency.vo.AgencyRechargeRecordVO;
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
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date: 2019-12-21
 * @Version: V1.0
 */
@Service
public class AgencyRechargeRecordServiceImpl extends ServiceImpl<AgencyRechargeRecordMapper, AgencyRechargeRecord> implements IAgencyRechargeRecordService {
    @Autowired(required = false)
    private AgencyRechargeRecordMapper agencyRechargeRecordMapper;
    @Autowired
    private IAgencyManageService iAgencyManageService;
    @Autowired
    private IAgencyBankCardService iAgencyBankCardService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    @Lazy
    private IAgencySettleAccountsService iAgencySettleAccountsService;

    @Override
    public IPage<AgencyRechargeRecordDTO> getAgencyRechargeRecordList(Page<AgencyRechargeRecord> page, AgencyRechargeRecordVO agencyRechargeRecordVO) {
        return agencyRechargeRecordMapper.getAgencyRechargeRecordList(page, agencyRechargeRecordVO);
    }

    @Override
    @Transactional
    public Result<AgencyRechargeRecordDTO> cashOut(JSONObject jsonObject) {
        Result<AgencyRechargeRecordDTO> result = new Result<>();
        String sysUserId = jsonObject.getString("sysUserId");//代理userId
        BigDecimal amount = jsonObject.getBigDecimal("amount");//交易金额
        String agencyBankCardId = jsonObject.getString("agencyBankCardId");//银行卡id
        String remark = jsonObject.getString("remark");//备注
        if (StringUtils.isBlank(agencyBankCardId)) {
            return result.error500("请先设置银行卡");
        }
        //获取代理
        AgencyManage agencyManage = iAgencyManageService.getOne(new LambdaQueryWrapper<AgencyManage>()
                .eq(AgencyManage::getSysUserId,sysUserId));
        if (oConvertUtils.isEmpty(agencyManage)){
            return result.error500("代理信息异常,请重新操作");
        }
        //判断代理余额
        if (amount.doubleValue() <= agencyManage.getBalance().doubleValue()) {
            //减去代理余额
            agencyManage.setBalance(agencyManage.getBalance().subtract(amount));
            agencyManage.setUnusableFrozen(agencyManage.getUnusableFrozen().add(amount));
            iAgencyManageService.updateById(agencyManage);
        } else {
            return result.error500("店铺余额不足");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        AgencyBankCard agencyBankCard = iAgencyBankCardService.getById(agencyBankCardId);
        if (StringUtils.isBlank(agencyBankCard.getId())) {
            return result.error500("银行卡信息异常,请重新设置银行卡信息");
        }
        //获取手续费比例
        String s = StringUtils.substringBefore(iSysDictService.queryTableDictTextByKey("sys_dict_item",
                "item_value", "item_text", "withdrawal_service_charge"), "%");
        SysUser user = iSysUserService.getById(sysUserId);
        AgencySettleAccounts agencySettleAccounts = new AgencySettleAccounts();
        agencySettleAccounts.setDelFlag("0");
        agencySettleAccounts.setSysUserId(agencyManage.getSysUserId());
        agencySettleAccounts.setOrderNo(OrderNoUtils.getOrderNo());
        agencySettleAccounts.setPhone(user.getPhone());
        agencySettleAccounts.setMoney(amount);
        if (agencyBankCard.getCarType().equals("0")) {
            agencySettleAccounts.setWithdrawalType("2");//提现类型(银行卡)
        } else if (agencyBankCard.getCarType().equals("1")) {
            agencySettleAccounts.setWithdrawalType("1");//提现类型(支付宝)
        } else {
            agencySettleAccounts.setWithdrawalType("0");//提现类型(微信)
        }
        //手续费
        BigDecimal c = amount.multiply(new BigDecimal(s).divide(new BigDecimal(100)));
        agencySettleAccounts.setServiceCharge(c);//手续费
        agencySettleAccounts.setAmount(amount.subtract(c));//实际金额
        agencySettleAccounts.setTimeApplication(new Date());//申请时间
        agencySettleAccounts.setStatus("0");//状态
        agencySettleAccounts.setRemark(remark);//备注
        agencySettleAccounts.setBankCard(agencyBankCard.getBankCard());//银行卡号(支付宝账号)
        agencySettleAccounts.setBankName(agencyBankCard.getBankName());//开户行名称
        agencySettleAccounts.setCardholder(agencyBankCard.getCardholder());//持卡人姓名(真实姓名)
        iAgencySettleAccountsService.save(agencySettleAccounts);

        boolean b = this.save(new AgencyRechargeRecord()
                .setDelFlag("0")
                .setSysUserId(sysUserId)
                .setPayType("1")
                .setGoAndCome("1")
                .setAmount(amount)
                .setOrderNo(OrderNoUtils.getOrderNo())
                .setTradeStatus("1")
                .setTradeNo(agencySettleAccounts.getOrderNo())
                .setBankCard(agencyBankCard.getBankCard())
                .setBankName(agencyBankCard.getBankName())
                .setCardholder(agencyBankCard.getCardholder())
                .setRemark("余额提现["+agencySettleAccounts.getOrderNo()+"]")
        );
        if (b) {
            result.setCode(200);
            result.setMessage("操作成功,待审核!");
        } else {
            result.error500("操作失败!");
        }
        return result;
    }

    @Override
    public IPage<AgencyRechargeRecordVO> queryPageList(Page<AgencyRechargeRecord> page, AgencyRechargeRecordDTO agencyRechargeRecordDTO) {
        return baseMapper.queryPageList(page,agencyRechargeRecordDTO);
    }

    ;

}
