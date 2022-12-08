package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.system.entity.SysBlance;
import org.jeecg.modules.system.entity.SysBlanceCapitalDetail;
import org.jeecg.modules.system.mapper.SysBlanceMapper;
import org.jeecg.modules.system.service.ISysBlanceCapitalDetailService;
import org.jeecg.modules.system.service.ISysBlanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @Description: 平台余额
 * @Author: jeecg-boot
 * @Date:   2021-04-20
 * @Version: V1.0
 */
@Service
public class SysBlanceServiceImpl extends ServiceImpl<SysBlanceMapper, SysBlance> implements ISysBlanceService {

    @Autowired
    private ISysBlanceCapitalDetailService iSysBlanceCapitalDetailService;

    @Override
    @Transactional
    public void add(BigDecimal balance,String tradeType,String tradeNo) {
        //加入资金只有大于0才被记录
        if(balance.doubleValue()>0) {
            //获取一个余额对象
            SysBlance sysBlance = this.getOne(new LambdaQueryWrapper<>());
            if (sysBlance == null) {
                sysBlance = new SysBlance().setBalance(new BigDecimal(0));
                this.save(sysBlance);
            }
            //创建余额记录
            SysBlanceCapitalDetail sysBlanceCapitalDetail = new SysBlanceCapitalDetail();
            sysBlanceCapitalDetail.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            sysBlanceCapitalDetail.setTradeType(tradeType);//交易类型;0:订单利润；1：利润退款；2：积分回购
            sysBlanceCapitalDetail.setGoAndCome("0");//支付和收入；0：收入；1：支出
            sysBlanceCapitalDetail.setAmount(balance);//交易额
            sysBlance.setBalance(sysBlance.getBalance().add(balance).setScale(2, RoundingMode.DOWN));
            sysBlanceCapitalDetail.setBlance(sysBlance.getBalance());//账户余额
            sysBlanceCapitalDetail.setPayTime(new Date());//交易时间
            sysBlanceCapitalDetail.setTradeNo(tradeNo);//交易单号
            this.saveOrUpdate(sysBlance);
            iSysBlanceCapitalDetailService.save(sysBlanceCapitalDetail);
        }
    }

    @Override
    @Transactional
    public void subtract(BigDecimal balance,String tradeType,String tradeNo) {
        //减少资金只有大于0才被记录
        if(balance.doubleValue()>0) {
            //获取一个余额对象
            SysBlance sysBlance = this.getOne(new LambdaQueryWrapper<>());
            if (sysBlance == null) {
                sysBlance = new SysBlance().setBalance(new BigDecimal(0));
                this.save(sysBlance);
            }
            //创建余额记录
            SysBlanceCapitalDetail sysBlanceCapitalDetail = new SysBlanceCapitalDetail();
            sysBlanceCapitalDetail.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            sysBlanceCapitalDetail.setTradeType(tradeType);//交易类型;0:订单利润；1：利润退款；2：积分回购
            sysBlanceCapitalDetail.setGoAndCome("1");//支付和收入；0：收入；1：支出
            sysBlanceCapitalDetail.setAmount(balance);//交易额
            sysBlance.setBalance(sysBlance.getBalance().subtract(balance).setScale(2, RoundingMode.DOWN));
            sysBlanceCapitalDetail.setBlance(sysBlance.getBalance());//账户余额
            sysBlanceCapitalDetail.setPayTime(new Date());//交易时间
            sysBlanceCapitalDetail.setTradeNo(tradeNo);//交易单号
            this.saveOrUpdate(sysBlance);
            iSysBlanceCapitalDetailService.save(sysBlanceCapitalDetail);
        }
    }

    @Override
    public BigDecimal getBalance() {
        //获取一个余额对象
        SysBlance sysBlance = this.getOne(new LambdaQueryWrapper<>());
        if (sysBlance == null) {
            sysBlance = new SysBlance().setBalance(new BigDecimal(0));
            this.save(sysBlance);
        }
        return sysBlance.getBalance();
    }
}
