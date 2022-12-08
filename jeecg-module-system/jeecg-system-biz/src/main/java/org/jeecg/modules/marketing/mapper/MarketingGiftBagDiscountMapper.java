package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagDiscount;

import java.util.List;

/**
 * @Description: 礼包优惠券券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface MarketingGiftBagDiscountMapper extends BaseMapper<MarketingGiftBagDiscount> {

    List<MarketingGiftBagDiscountDTO> findDiscountById(@Param("marketingGiftBagId") String marketingGiftBagId);

}
