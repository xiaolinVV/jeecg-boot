package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingFreeOrder;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单订单
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeOrderService extends IService<MarketingFreeOrder> {

    /**
     * 商品订单id
     * @param orderId
     * @return
     */
    public boolean submitOrder(String orderId);


    /**
     * 场次分组订单信息
     *
     * 张靠勤   2021-3-18
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeOrderGroupByPayTime(Map<String,Object> paramMap);



    /**
     * 根据场次id查询订单信息
     *
     * 张靠勤   2021-3-18
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeOrderByMarketingFreeSessionId(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    /**
     * 将免单的订单进行免单处理
     *
     * 张靠勤   2021-3-19
     *
     * @param marketingFreeOrderId
     */
    public String freeChargeOrder(String marketingFreeOrderId);

}
