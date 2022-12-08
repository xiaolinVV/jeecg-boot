package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagDiscount;

import java.util.List;

/**
 * @Description: 礼包优惠券券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IMarketingGiftBagDiscountService extends IService<MarketingGiftBagDiscount> {

    List<MarketingGiftBagDiscountDTO> findDiscountById(String marketingGiftBagId);

}
