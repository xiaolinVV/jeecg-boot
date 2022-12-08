package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.entity.MarketingChannelDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscount;

import java.util.List;

/**
 * @Description: 发券渠道和店铺券关系
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface MarketingChannelDiscountMapper extends BaseMapper<MarketingChannelDiscount> {

    /**
     * 根据优惠券id查询查找所有优惠券的投放渠道
     * @param marketingChannelId
     * @return
     */
    public List<MarketingDiscount> getMarketingDiscountSoreByMarketingChannelId(@Param("marketingChannelId") String marketingChannelId);

    /**
     * 根据投放渠道ID查询优惠券
     * @param marketingDiscountId
     * @return
     */
    public List<MarketingChannel>getMarketingChannelByMarketingDiscountSoreId(@Param("marketingDiscountId") String marketingDiscountId);
}
