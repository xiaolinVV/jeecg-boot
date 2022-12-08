package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;
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
public interface MarketingDiscountGoodMapper extends BaseMapper<MarketingDiscountGood> {
    /**
     * 平台适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    List<MarketingDisountGoodDTO> findGood(@Param("marketingDiscountId") String marketingDiscountId);

    /**
     * 店铺适用商品信息
     * @param
     * @param marketingDiscountId
     * @return
     */
    List<MarketingDisountGoodDTO> findStoreGood(@Param("marketingDiscountId") String marketingDiscountId);

    List<Map<String,Object>> findMarketingDiscountGoodById(@Param("id") String id);

    List<Map<String,Object>> findMarketingDiscountStoreGoodById(@Param("id") String id);

    IPage<Map<String,Object>> findMarketingDiscountStoreGoodPageListById(Page<Map<String,Object>> page,@Param("id") String id);

    IPage<Map<String,Object>> findMarketingDiscountGoodPageListById(Page<Map<String,Object>> page,@Param("id") String id);
}
