package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountGood;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
public interface IMarketingDiscountGoodService extends IService<MarketingDiscountGood> {

    /**
     * 平台适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    List<MarketingDisountGoodDTO> findGood(String marketingDiscountId);

    /**
     * 店铺适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    List<MarketingDisountGoodDTO> findStoreGood(String marketingDiscountId);

    List<Map<String,Object>> findMarketingDiscountGoodById(String id);

    List<Map<String,Object>> findMarketingDiscountStoreGoodById(String id);

    IPage<Map<String,Object>> findMarketingDiscountGoodPageListById(Page<Map<String,Object>> page, String id);

    IPage<Map<String,Object>> findMarketingDiscountStoreGoodPageListById(Page<Map<String,Object>> page, String id);
}
