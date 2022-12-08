package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagDiscount;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagDiscountMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 礼包优惠券券映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagDiscountServiceImpl extends ServiceImpl<MarketingGiftBagDiscountMapper, MarketingGiftBagDiscount> implements IMarketingGiftBagDiscountService {
    @Autowired(required = false)
    private MarketingGiftBagDiscountMapper marketingGiftBagDiscountMapper;
    @Override
    public List<MarketingGiftBagDiscountDTO> findDiscountById(String marketingGiftBagId) {
        return marketingGiftBagDiscountMapper.findDiscountById(marketingGiftBagId);
    }
}
