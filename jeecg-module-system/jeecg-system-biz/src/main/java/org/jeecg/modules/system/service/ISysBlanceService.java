package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysBlance;

import java.math.BigDecimal;

/**
 * @Description: 平台余额
 * @Author: jeecg-boot
 * @Date:   2021-04-20
 * @Version: V1.0
 */
public interface ISysBlanceService extends IService<SysBlance> {


    /**
     * 平台金额增加
     *
     * @param balance 金额
     * @param tradeType 交易类型;0:订单利润；1：利润退款；2：积分回购
     * @param tradeNo  单号
     */
    public void add(BigDecimal balance,String tradeType,String tradeNo);

    /**
     * 平台金额减少
     *
     * @param balance 金额
     * @param tradeType 交易类型;0:订单利润；1：利润退款；2：积分回购
     * @param tradeNo  单号
     */
    public void subtract(BigDecimal balance,String tradeType,String tradeNo);


    /**
     * 获取当前平台金额
     *
     * @return
     */
    public BigDecimal getBalance();
}
