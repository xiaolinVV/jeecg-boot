package org.jeecg.modules.pay.service.impl;

import org.jeecg.modules.pay.entity.PayBalanceLog;
import org.jeecg.modules.pay.mapper.PayBalanceLogMapper;
import org.jeecg.modules.pay.service.IPayBalanceLogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 余额支付日志
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Service
public class PayBalanceLogServiceImpl extends ServiceImpl<PayBalanceLogMapper, PayBalanceLog> implements IPayBalanceLogService {

}
