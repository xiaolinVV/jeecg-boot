package org.jeecg.modules.quanhuifu;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 张少林
 * @date 2023年04月28日 10:51 上午
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class HftxPayUtilsTest {

    @Autowired
    HftxPayUtils hftxPayUtils;

    @Autowired
    IOrderStoreGoodRecordService orderStoreGoodRecordService;

    @Test
    public void testGetSettleAccountBalance() throws BaseAdaPayException {
        Map<String, Object> balanceMap = this.hftxPayUtils.getSettleAccountBalance();
        if (!balanceMap.get("status").equals("succeeded")) {
            return;
        }
        Object var10000 = balanceMap.get("avl_balance");
        log.info("汇付商户余额：{}", var10000);
    }

    @Test
    public void name() {
        LambdaQueryWrapper<OrderStoreGoodRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderStoreGoodRecord::getOrderStoreSubListId,"7274366e577180ceab9864711ebf32d6");
        List<OrderStoreGoodRecord> marketingOrderStoreGoodRecords = orderStoreGoodRecordService.list(lambdaQueryWrapper);
        // 更新订单商品表扣除优惠券后的实付金额 @zhangshaolin
        if (CollUtil.isNotEmpty(marketingOrderStoreGoodRecords)) {
            //订单优惠后实付款，不含运费
            BigDecimal actualPayment = new BigDecimal("2862");
            BigDecimal marketingTotalPrice = new BigDecimal("3762");
            BigDecimal tempSum = new BigDecimal(0);
            for (int i = 0; i < marketingOrderStoreGoodRecords.size(); i++) {
                OrderStoreGoodRecord orderStoreGoodRecord = marketingOrderStoreGoodRecords.get(i);
                BigDecimal total = orderStoreGoodRecord.getTotal();
                if (i == marketingOrderStoreGoodRecords.size() - 1) {
                    BigDecimal orderGoodActualPayment = NumberUtil.sub(actualPayment,tempSum);
                    log.info(orderGoodActualPayment.toString());
                    orderStoreGoodRecord.setCustomaryDues(orderGoodActualPayment);
                    orderStoreGoodRecord.setActualPayment(orderGoodActualPayment);
                    orderStoreGoodRecord.setCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(),orderStoreGoodRecord.getActualPayment()));
                    orderStoreGoodRecord.setTotalCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(),orderStoreGoodRecord.getActualPayment()));
                } else {
                    BigDecimal orderGoodActualPayment = NumberUtil.mul(NumberUtil.div(total, marketingTotalPrice,2), actualPayment);
                    log.info(orderGoodActualPayment.toString());
                    orderStoreGoodRecord.setCustomaryDues(orderGoodActualPayment);
                    orderStoreGoodRecord.setActualPayment(orderGoodActualPayment);
                    orderStoreGoodRecord.setCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(),orderStoreGoodRecord.getActualPayment()));
                    orderStoreGoodRecord.setTotalCoupon(NumberUtil.sub(orderStoreGoodRecord.getTotal(),orderStoreGoodRecord.getActualPayment()));
                    tempSum = NumberUtil.add(tempSum,orderGoodActualPayment);
                }
            }
        }
    }
}
