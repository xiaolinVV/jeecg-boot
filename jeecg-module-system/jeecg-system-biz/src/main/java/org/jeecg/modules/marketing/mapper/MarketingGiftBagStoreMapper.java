package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStore;
import org.jeecg.modules.store.dto.StoreManageDTO;

import java.util.List;

/**
 * @Description: 礼包店铺映射
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface MarketingGiftBagStoreMapper extends BaseMapper<MarketingGiftBagStore> {

    List<StoreManageDTO> findStoreById(@Param("marketingGiftBagId") String marketingGiftBagId);
}
