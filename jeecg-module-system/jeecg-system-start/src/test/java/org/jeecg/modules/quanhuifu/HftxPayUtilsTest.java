package org.jeecg.modules.quanhuifu;

import com.huifu.adapay.core.exception.BaseAdaPayException;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void testGetSettleAccountBalance() throws BaseAdaPayException {
        Map<String, Object> balanceMap = this.hftxPayUtils.getSettleAccountBalance();
        if (!balanceMap.get("status").equals("succeeded")) {
            return;
        }
        Object var10000 = balanceMap.get("avl_balance");
        log.info("汇付商户余额：{}", var10000);
    }
}
